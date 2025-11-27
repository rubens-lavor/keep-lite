import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';

// O k6 lê este arquivo uma vez e compartilha a memória entre os VUs.
const users = new SharedArray('users', function () {
  return JSON.parse(open('./users.json'));
});

// Opções do teste: 120 usuários virtuais (VUs), cada um executará o script 1 vez.
export const options = {
  vus: 120,
  iterations: 120, // 20 VUs, 20 iterações no total (uma por VU)
};

export default function () {
  const baseUrl = 'http://localhost:8080/api/v1';

  // Cada usuário virtual (VU) pega suas próprias credenciais do array compartilhado.
  const user = users[__VU - 1];
  
  // --- PASSO A: FAZER LOGIN ---
  const loginPayload = JSON.stringify({
    email: user.email,
    password: user.password,
  });

  const params = {
    headers: { 'Content-Type': 'application/json' },
  };

  const loginRes = http.post(`${baseUrl}/auth/login`, loginPayload, params);

  check(loginRes, {
    'logged in successfully': (r) => r.status === 200,
  });
  
  // Extrai o accessToken da resposta de login
  const accessToken = loginRes.json('accessToken');

  if (!accessToken) {
    console.error(`Não foi possível logar o usuário ${user.email}, pulando a criação de notas.`);
    return; // Pula para o próximo VU se o login falhar
  }
  
  console.log(`Usuário ${user.email} logado. Iniciando criação de 50 notas...`);

  // --- PASSO B: CRIAR 50 NOTAS ---
  const authParams = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${accessToken}`, // Usa o token obtido
    },
  };

  for (let i = 0; i < 50; i++) {
    const notePayload = JSON.stringify({
      title: `Nota ${i + 1} de ${user.email}`,
      content: `Este é o conteúdo da nota de teste número ${i + 1}.`,
    });

    const createNoteRes = http.post(`${baseUrl}/notes`, notePayload, authParams);
    
    check(createNoteRes, {
      'note created successfully': (r) => r.status === 201,
    });

    sleep(0.5); 
  }
}

import http from 'k6/http';

// Esta função roda uma única vez no início do teste
export function setup() {
  console.log('Iniciando a criação de 120 usuários...');
  const baseUrl = 'http://localhost:8080/api/v1';
  const createdUsers = [];

  for (let i = 0; i < 120; i++) {
    const userEmail = `user${i}@keeplite.com`;
    const userName = `User ${i}`;
    const userPassword = 'password123';

    const payload = JSON.stringify({
      name: userName,
      email: userEmail,
      password: userPassword,
    });
    const params = { headers: { 'Content-Type': 'application/json' } };
    const res = http.post(`${baseUrl}/user/register`, payload, params);

    if (res.status === 201) {
      console.log(`-> Usuário ${userEmail} criado com sucesso.`);
      createdUsers.push({ email: userEmail, password: userPassword });
    } else {
      console.error(`-> Falha ao criar ${userEmail}. Status: ${res.status}`);
    }
  }
  // Retorna os dados para serem usados em outras fases
  return { users: createdUsers };
}

// A função default (VU) pode ficar vazia ou ser usada para outra coisa.
// Como só queremos criar os dados, não precisamos dela.
export default function (data) {}

export function teardown(data) {
  console.log(`\n`);
  console.log(`Criação finalizada. Total de ${data.users.length} usuários.`);
}

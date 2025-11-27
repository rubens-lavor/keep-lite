import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import { Trend } from 'k6/metrics';

// Carrega usuários de teste
const users = new SharedArray('users', function () {
  return JSON.parse(open('./users.json'));
});

// Métricas customizadas
const loginTime = new Trend('login_duration');
const getNotesTime = new Trend('get_notes_duration');

// Tokens armazenados por VU
const tokens = {};

// Opções do teste
export const options = {
  stages: [
    { duration: '30s', target: 120 }, // Rampa rápida para 120 VUs
    { duration: '2m', target: 120 },  // Sustentado por 2 minutos
    { duration: '10s', target: 0 },   // Descida
  ],
  thresholds: {
    'http_req_failed': ['rate<0.01'],
    'get_notes_duration': ['p(95)<100'], // esperar < 100ms
  },
};

export default function () {
  const baseUrl = 'http://localhost:8080/api/v1';
  const vuId = __VU;
  let token = tokens[vuId];

  // --- LOGIN ---
  if (!token) {
    const user = users[(vuId - 1) % users.length];
    const loginPayload = JSON.stringify({
      email: user.email,
      password: user.password,
    });
    const loginParams = { headers: { 'Content-Type': 'application/json' } };
    const loginRes = http.post(`${baseUrl}/auth/login`, loginPayload, loginParams);

    loginTime.add(loginRes.timings.duration);
    check(loginRes, { 'Login bem-sucedido': (r) => r.status === 200 });

    token = loginRes.json('accessToken');
    if (!token) return;
    tokens[vuId] = token;
  }

  const authHeaders = { headers: { Authorization: `Bearer ${token}` } };

  // --- BUSCAR NOTAS (LOOP DE LEITURA INTENSIVA) ---
  // O usuário vai ler 10x antes de "pensar"
  for (let i = 0; i < 10; i++) {
    const getRes = http.get(`${baseUrl}/notes`, authHeaders);
    getNotesTime.add(getRes.timings.duration);
    check(getRes, { 'Buscou notas com sucesso': (r) => r.status === 200 });
    sleep(0.1); // Pausa curta entre as leituras
  }

  // --- Tempo de "pensamento" do usuário ---
  sleep(Math.random() * 2 + 1); // Espera entre 1 e 3 segundos antes de repetir o loop
}
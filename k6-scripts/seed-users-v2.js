import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';

const baseUrl = 'http://localhost:8080/api/v1';

// o k6 lê o arquivo uma única vez em tempo de init e compartilha entre VUs
const users = new SharedArray('users', () => JSON.parse(open('./users.json')));

// 1 VU por usuário, 1 iteração por VU (idempotente)
export const options = {
  vus: users.length,
  iterations: users.length,
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<800'],
  },
};

export default function () {
  // cada VU pega seu usuário
  // __VU é uma variável global do k6, é um usuário virtual que está executando o script
  const user = users[__VU - 1];

  console.log(`VU #${__VU} usando o usuário ${user.email}`);

  const payload = JSON.stringify({
    name: user.email.split('@')[0],
    email: user.email,
    password: user.password,
  });

  const params = { headers: { 'Content-Type': 'application/json' } };

  const res = http.post(`${baseUrl}/user/register`, payload, params);

//  // aceite 201 (criado) e 409 (já existe) para idempotência do seed
//  const ok = check(res, {
//    '201': (r) => r.status === 201,
//    '409 Already exists (ok)': (r) => r.status === 409,
//  });
//
//  if (!ok) {
//    console.error(`Falha ao criar ${user.email}: ${res.status} ${res.body}`);
//  } else {
//    console.log(`${user.email} criado com sucesso`);
//  }

  if (res.status === 201) {
    console.log(`-> Usuário ${user.email} criado com sucesso.`);
  } else {
    console.error(`-> Falha ao criar ${user.email}. Status: ${res.status} ${res.body}`);
  }
  sleep(0.1);
}

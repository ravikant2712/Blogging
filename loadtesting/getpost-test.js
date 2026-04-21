import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 100,       //users
  duration: '10s',    //time
};

const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYXZpa2FudDI3MTJAZ21haWwuY29tIiwiaWF0IjoxNzc2NDA4NzIwLCJleHAiOjE3NzY0NDQ3MjB9.nUj8qs8-_wurETDpZajOBYqYVHqC7z0fNuNu6NPJ5BU';

export default function () {
  const url = 'http://localhost:8080/api/posts';

  const params = {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  };

  let res = http.get(url, params);
  console.log(res.status);
//  console.log(res.body);
  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1);
}
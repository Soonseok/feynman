import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // 프록시 설정 추가
    proxy: {
      '/api': {
        target: 'http://localhost:8505', // 스프링 서버 주소와 포트
        changeOrigin: true, // CORS 문제 방지
        secure: false, // HTTPS가 아닌 HTTP 사용 시 필요
        //rewrite: (path) => path.replace(/^\/api/, ''), // 필요하다면 경로 재작성
      },
    },
  },
})

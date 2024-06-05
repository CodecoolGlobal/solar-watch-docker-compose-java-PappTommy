import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,
    strictPort:true,
    port:5173,
    proxy: {
      '/solar-user': 'http://solar-container:8080'             //'http://localhost:8080'
    },
  },
})

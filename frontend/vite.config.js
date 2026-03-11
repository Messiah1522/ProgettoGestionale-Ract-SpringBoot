import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import path from 'node:path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      'framer-motion': path.resolve(__dirname, 'src/shims/framer-motion.jsx'),
      'lucide-react': path.resolve(__dirname, 'src/shims/lucide-react.jsx'),
    },
  },
})

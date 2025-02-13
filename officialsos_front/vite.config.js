import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // server: {
  //   proxy: {
  //     '/api': {
  //       target: 'http://98.84.14.117:8080',
  //       changeOrigin: true
  //     }
  //   }
  // }
});

// export default defineConfig({
//   server: {
//     proxy: {
//       '/api': {
//         target: 'http://your-backend-url.com',
//         changeOrigin: true,
//         rewrite: (path) => path.replace(/^\/api/, ''),
//       },
//     },
//   },
// });

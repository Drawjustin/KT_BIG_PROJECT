import './App.css';
import { BrowserRouter as Router } from 'react-router-dom';
import { Provider } from 'react-redux';

import store from './store';
import Layout from './_components/layout/Layout';
import Navbar from './_components/navbar/Navbar';
import Footer from './_components/footer/Footer';
import AppRoutes from './routes';
function App() {
  return (
    <Provider store={store}>
        <Router>
          <Layout>
            <Navbar />
            <AppRoutes />
          </Layout>
          <Footer />
        </Router>
    </Provider>
  );
}

export default App;
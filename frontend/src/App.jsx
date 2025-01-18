import './App.css';
import Dashboard from './Component/Dashboard';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navigation from './Component/Navigation';
import ProtectedRoute from './Component/ProtectedRoute';
import Login from './Component/Login';
import Products from './Component/Products';
import Search from './Component/Search';
import SearchResult from './Component/SearchResult';
import Category from './Component/Category';

const ProtectedPage = ({ children }) => (
  <ProtectedRoute>
    <Navigation />
    <Search />
    {children}
  </ProtectedRoute>
);

function App() {
  return (
    <Router future={{ v7_relativeSplatPath: true , v7_startTransition: true }}>
      <Routes>
        <Route 
          path='/' 
          element={<Login />} 
        />

        <Route 
          path='/dashboard' 
          element={
            <ProtectedPage>
              <Category />
              <Dashboard />
            </ProtectedPage>
          } 
        />

        <Route
          path='/category'
          element={
            <ProtectedPage>
              <Category />
              <Dashboard />
            </ProtectedPage>
          }
        />

        <Route 
          path='/products/:id' 
          element={
            <ProtectedPage>
              <Products />
            </ProtectedPage>
          } 
        />
        
        <Route 
          path='/products' 
          element={
            <ProtectedPage>
              <Products />
            </ProtectedPage>
          } 
        />

        <Route 
          path='/search/:search' 
          element={
            <ProtectedPage>
              <SearchResult />
            </ProtectedPage>
          } 
        />
      </Routes>
    </Router>
  );
}

export default App;

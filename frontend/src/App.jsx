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

function App() {
  return (
    <Router>
      <Routes>
        <Route 
          path='/' 
          element={<Login />} 
        />

        <Route 
          path='/dashboard' 
          element={
            <ProtectedRoute>
              <Navigation />
              <Search />
              <Category />
              <Dashboard />
            </ProtectedRoute>
          } 
        />

        <Route
          path='/category'
          element={
            <ProtectedRoute>
              <Navigation />
              <Search />
              <Category />
              <Dashboard />
            </ProtectedRoute>
          }
          />

        <Route 
          path='/products/:id' 
          element={
            <ProtectedRoute>
              <Navigation />
              <Search />
              <Products />
            </ProtectedRoute>
          } 
        />
        
        <Route 
          path='/products' 
          element={
            <ProtectedRoute>
              <Navigation />
              <Search />
              <Products />
            </ProtectedRoute>
          } 
        />

        <Route 
          path='/search/:search' 
          element={
            <ProtectedRoute>
              <Navigation />
              <Search />
              <SearchResult />
            </ProtectedRoute>
          } 
        />
      </Routes>
    </Router>
  );
}

export default App;

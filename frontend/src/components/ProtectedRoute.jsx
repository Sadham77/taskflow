import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

// Redirects to /login if there's no logged-in user, otherwise renders the page
export default function ProtectedRoute({ children }) {
  const { user } = useAuth()
  if (!user) {
    return <Navigate to="/login" replace />
  }
  return children
}

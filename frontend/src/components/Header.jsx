import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Header() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  if (!user) return null

  function handleLogout() {
    logout()
    navigate('/login')
  }

  return (
    <div className="app-header">
      <strong>TaskFlow</strong>
      <div>
        <span style={{ marginRight: 12 }}>{user.name}</span>
        <button onClick={handleLogout}>Log Out</button>
      </div>
    </div>
  )
}

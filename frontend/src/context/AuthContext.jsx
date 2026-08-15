import { createContext, useContext, useState } from 'react'
import api from '../api/client'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user')
    return saved ? JSON.parse(saved) : null
  })

  async function login(email, password) {
    const res = await api.post('/auth/login', { email, password })
    persistSession(res.data)
  }

  async function register(name, email, password) {
    const res = await api.post('/auth/register', { name, email, password })
    persistSession(res.data)
  }

  function persistSession(data) {
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify({ name: data.name, email: data.email }))
    setUser({ name: data.name, email: data.email })
  }

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}

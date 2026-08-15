import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/client'

export default function BoardsPage() {
  const [boards, setBoards] = useState([])
  const [title, setTitle] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    loadBoards()
  }, [])

  async function loadBoards() {
    const res = await api.get('/boards')
    setBoards(res.data)
  }

  async function handleCreate(e) {
    e.preventDefault()
    if (!title.trim()) return
    await api.post('/boards', { title })
    setTitle('')
    loadBoards()
  }

  return (
    <div className="boards-page">
      <h2>Your Boards</h2>

      <form className="new-board-form" onSubmit={handleCreate}>
        <input
          placeholder="New board title..."
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <button type="submit">Create Board</button>
      </form>

      <div className="board-grid">
        {boards.map((board) => (
          <div
            key={board.id}
            className="board-card"
            onClick={() => navigate(`/boards/${board.id}`)}
          >
            {board.title}
          </div>
        ))}
      </div>
    </div>
  )
}

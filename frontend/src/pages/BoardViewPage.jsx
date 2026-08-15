import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { DragDropContext, Droppable, Draggable } from '@hello-pangea/dnd'
import api from '../api/client'

export default function BoardViewPage() {
  const { boardId } = useParams()
  const [lists, setLists] = useState([])
  const [newListTitle, setNewListTitle] = useState('')

  useEffect(() => {
    loadLists()
  }, [boardId])

  async function loadLists() {
    const res = await api.get(`/boards/${boardId}/lists`)
    setLists(res.data)
  }

  async function handleAddList(e) {
    e.preventDefault()
    if (!newListTitle.trim()) return
    await api.post(`/boards/${boardId}/lists`, { title: newListTitle })
    setNewListTitle('')
    loadLists()
  }

  async function handleAddCard(listId, title) {
    if (!title.trim()) return
    await api.post(`/lists/${listId}/cards`, { title })
    loadLists()
  }

  // Fires when a drag ends - this is what keeps the UI and backend in sync
  async function handleDragEnd(result) {
    const { source, destination, draggableId } = result
    if (!destination) return // dropped outside any list

    // Optimistically update local state so the UI feels instant
    const newLists = lists.map((list) => ({ ...list, cards: [...list.cards] }))
    const sourceList = newLists.find((l) => l.id.toString() === source.droppableId)
    const destList = newLists.find((l) => l.id.toString() === destination.droppableId)
    const [movedCard] = sourceList.cards.splice(source.index, 1)
    destList.cards.splice(destination.index, 0, movedCard)
    setLists(newLists)

    // Persist the move on the backend
    await api.put(`/cards/${draggableId}/move`, {
      targetListId: Number(destination.droppableId),
      newPosition: destination.index,
    })
  }

  return (
    <div className="board-view">
      <form className="add-list-form" onSubmit={handleAddList} style={{ marginBottom: 20 }}>
        <input
          placeholder="New list title..."
          value={newListTitle}
          onChange={(e) => setNewListTitle(e.target.value)}
        />
        <button type="submit">Add List</button>
      </form>

      <DragDropContext onDragEnd={handleDragEnd}>
        <div className="lists-row">
          {lists.map((list) => (
            <Droppable droppableId={list.id.toString()} key={list.id}>
              {(provided) => (
                <div
                  className="list-column"
                  ref={provided.innerRef}
                  {...provided.droppableProps}
                >
                  <h4>{list.title}</h4>

                  {list.cards.map((card, index) => (
                    <Draggable
                      draggableId={card.id.toString()}
                      index={index}
                      key={card.id}
                    >
                      {(provided) => (
                        <div
                          className="card-item"
                          ref={provided.innerRef}
                          {...provided.draggableProps}
                          {...provided.dragHandleProps}
                        >
                          <h5>{card.title}</h5>
                          {card.dueDate && <p>Due: {card.dueDate}</p>}
                        </div>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}

                  <NewCardForm onAdd={(title) => handleAddCard(list.id, title)} />
                </div>
              )}
            </Droppable>
          ))}
        </div>
      </DragDropContext>
    </div>
  )
}

function NewCardForm({ onAdd }) {
  const [title, setTitle] = useState('')

  function handleSubmit(e) {
    e.preventDefault()
    onAdd(title)
    setTitle('')
  }

  return (
    <form className="add-card-form" onSubmit={handleSubmit}>
      <input
        placeholder="Add a card..."
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <button type="submit">+</button>
    </form>
  )
}

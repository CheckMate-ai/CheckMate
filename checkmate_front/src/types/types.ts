
interface VerifyRequest {
  id: number
  input: string
}

interface Website{
  favicon_link: string
  name: string
  link: string
}

interface Source {
  id: string
  website: Website,
  title: string,
  article_snippet: string,
  image_preview_link: string,
  safe: boolean
  date: Date
}

interface TextualMessage {
  content: string
  from: "User" | "AI"
  created_at: Date
}

type Message = Source | TextualMessage

interface Conversation {
  title: string
  origin: Website,
  messages: Message[]
  request_id: number
  created_at: Date
}
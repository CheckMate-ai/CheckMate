
interface VerifyRequest {
  id: number
  input: string
}

interface SourceWebsite{
  favicon_link: string
  name: string
}

interface Source {
  id: string
  website: SourceWebsite,
  link: string
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
  messages: Message[]
  request_id: number
  created_at: Date
}
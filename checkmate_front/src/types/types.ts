
interface VerifyRequest {
  id: number
  input: string
}

interface IWebsite{
  favicon_link: string
  name: string
  link: string
}

interface ISource {
  id: string
  website: IWebsite,
  title: string,
  article_snippet: string,
  image_preview_link: string,
  safe: boolean
  date?: Date
}

interface ITextualMessage {
  content: string
  from: "User" | "AI"
  created_at?: Date
}

type Message = ISource | ITextualMessage
type Trust = "Doubtful" | "Fake" | "Seems Legit"

interface IConversation {
  title: string
  origin: IWebsite
  trust: Trust
  messages: Message[]
  request_id: number
  created_at: Date
}

export type {IConversation, ISource, IWebsite, VerifyRequest}
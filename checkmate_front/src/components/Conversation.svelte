<script lang="ts">
  import type { IConversation } from "src/types/types";
  import "./conversation.css"
  import dayjs from "dayjs";
  import relativeTime from 'dayjs/plugin/relativeTime'
  export let convo: IConversation;
  import { conversation_details } from "../stores";

  dayjs.extend(relativeTime)
  const labelToIcon = {
    "Seems Legit": "legit.svg",
    "Doubtful": "doubt.svg",
    "Fake": "falsy.svg",
  }
  const labelToColor = {
    "Seems Legit": "#2ecc71",
    "Doubtful": "#f1c40f",
    "Fake": "#e74c3c",
  }
</script>
<div class="card">
  <div class="checking">
    <img src={`../../public/${labelToIcon[convo.trust]}`} width="32" alt="">
    <span style={`color: ${labelToColor[convo.trust]};`}><b><center>{convo.trust}</center></b></span>
  </div>
  <div>
    <h2>
      {convo.title.length <= 55 ? convo.title : convo.title.substring(0, 55) + "..."}
    </h2>
    <div class="origin">
      <img src={convo.origin.favicon_link} alt="" width="26" height="26">
      <a href={convo.origin.link} target="_blank">{convo.origin.name}</a>
      <p>{dayjs(convo.created_at).fromNow()}</p>
    </div>
  </div>
  <button class="open-details" on:click={(e) => conversation_details.set(convo)}>
    <img src="../../public/open.svg" width="13" alt="">
  </button>
</div>
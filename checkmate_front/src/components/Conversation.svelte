<script lang="ts">
  import { type IConversation } from "../types/types";
  import "./conversation.css";
  import dayjs from "dayjs";
  import relativeTime from "dayjs/plugin/relativeTime";
  export let convo: IConversation;
  export let detail_view: boolean;
  import { conversation_details } from "../stores";
  import Source from "./Source.svelte";

  dayjs.extend(relativeTime);
  const labelToIcon = {
    Legit: "legit.svg",
    Doubtful: "doubt.svg",
    Fake: "falsy.svg",
  };
  const labelToColor = {
    Legit: "#2ecc71",
    Doubtful: "#f1c40f",
    Fake: "#e74c3c",
  };

  const SNIPPET_MAX_LENGTH = 110;
  const LINK_MAX_LENGTH = 48;
</script>

{#if !detail_view}
  <div class="card">
    <div class="checking">
      <img src={`../../public/${labelToIcon[convo.trust]}`} width="32" alt="" />
      <span style={`color: ${labelToColor[convo.trust]};`}
        ><b><center>{convo.trust}</center></b></span
      >
    </div>
    <div>
      <h2>
        {convo.title.length <= 27
          ? convo.title
          : convo.title.substring(0, 24) + "..."}
      </h2>
      <div class="origin">
        <img src={convo.origin.favicon_link} alt="" width="26" height="26" />
        <a href={convo.origin.link} target="_blank">{convo.origin.name.length <= 18
          ? convo.origin.name
          : convo.origin.name.substring(0, 17) + "..."}</a>
        <p>{dayjs(convo.request_id).fromNow()}</p>
      </div>
    </div>
    <button
      class="open-details"
      on:click={(e) => conversation_details.set(convo)}
    >
      <img src="../../public/open.svg" width="13" alt="" />
    </button>
  </div>
{:else}
  <div class="conv-list">
    {#each convo.messages as message}
      {#if "article_snippet" in message}
        <Source source={message} />
      {:else}
        <div class={`bubble ${message.from == "User" ? "right" : "left"}`}>
          <span
            class={`${message.from == "User" ? "" : "type"}`}
            style={`--n:${message.content.length}`}>{message.content}</span
          >
        </div>
      {/if}
    {/each}
  </div>
{/if}

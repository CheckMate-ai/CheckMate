<script lang="ts">
  import { conversation_details } from "../stores";
  import Source from "../components/Source.svelte";
  import type { IConversation } from "src/types/types";

  export let conversation: IConversation;
</script>

<main>
  <section style="flex-direction: row; align-items:center;">
    <button class="close-details" on:click={(e) => conversation_details.set(null)}>
      <img src="../../public/open.svg" width="13" alt="">
    </button>
    <h1>Details</h1>
  </section>
  <div class="conv-list">
    {#each conversation.messages as message}
      {#if "article_snippet" in message}
        <Source source={message}/>
      {:else}
        <div class={`bubble ${message.from == "User" ? "right" : "left"}`}>
          <span class={`${message.from == "User" ? "" : "type"}`} style={`--n:${message.content.length}`}>{message.content}</span>
        </div>
      {/if}
    {/each}
  </div>
</main>

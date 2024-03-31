<script lang="ts">
  import { conversation_details } from "../stores";
  import type { IConversation } from "src/types/types";
  import Conversation from "../components/Conversation.svelte";

  export let conversation: IConversation;
</script>

<main>
  <section style="flex-direction: row; align-items:center;">
    <button
      class="close-details"
      on:click={(e) => conversation_details.set(null)}
    >
      <img src="../../public/open.svg" width="13" alt="" />
    </button>
    <h1>Details</h1>
  </section>
  <Conversation detail_view={true} convo={conversation} />
</main>

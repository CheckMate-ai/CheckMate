<script lang="ts">
  import "../styles/welcome.css";
  import Conversation from "../components/Conversation.svelte";
  import type { IConversation } from "../types/types";
  import Checking from "./Checking.svelte";
  import { conversation_details } from "../stores";

  let promise_history: Promise<IConversation[]> = fetch_history();
  let curr_convo: IConversation | null = null;
  async function fetch_history() {
    let { history } = await chrome.storage.local.get({history: []});
    return history;
  }

  conversation_details.subscribe((value) => {
    curr_convo = value;
  });
</script>

{#if curr_convo != null}
  <Checking conversation={curr_convo} />
{:else}
  <main>
    <section>
      <h1>Historique</h1>
      <div
        class="howto"
        style="display: flex; margin-bottom: 1rem; align-items: space-between;"
      >
        <img src="../../public/animation.gif" width="84" alt="" srcset="" />
        <span style="color: black;"> Surligner du texte et lancer la vérification.</span>
      </div>
    </section>
    <div class="conv-list">
      {#await promise_history}
        <img src="../../public/loading.gif" style="align-self: center;" alt="" width="96" srcset="" />
      {:then history}
        {#each history as convo}
          <Conversation {convo} detail_view={false} />
        {/each}
      {/await}
    </div>
  </main>

  <footer></footer>
{/if}

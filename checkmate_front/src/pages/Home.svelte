<script lang="ts">
  import "../styles/welcome.css";
  import Conversation from "../components/Conversation.svelte";
  import type { IConversation } from "../types/types";
  import Checking from "./Checking.svelte";
  import { onMount } from "svelte";
  import { conversation_details } from "../stores";

  let historic: IConversation[] = [];
  let curr_convo: IConversation | null = null;
  onMount(async () => {
    let storage_hist = (await chrome.storage.local.get({
      historic: [
        {
          title:
            "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Harum quos voluptas dolorem quis ex veritatis debitis nobis necessitatibus mollitia excepturi vel ea itaque officiis iusto adipisci ipsa, nam odio perspiciatis!",
          messages: [
            {
              content: "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Harum quos voluptas dolorem quis ex veritatis debitis nobis necessitatibus mollitia excepturi vel ea itaque officiis iusto adipisci ipsa, nam odio perspiciatis!",
              from: "User"
            },
            {
              content: "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Harum quos voluptas dolorem quis ex veritatis debitis nobis necessitatibus mollitia excepturi vel ea itaque officiis iusto adipisci ipsa, nam odio perspiciatis!",
              from: "AI"
            },
            {
              id: 1, 
              website: {
                name: "Mediapart",
                link: "https://www.mediapart.fr/journal/france/270324/emmanuel-macron-saupoudre-la-guyane-de-petites-annonces-distance-de-la-population",
                favicon_link: "https://www.google.com/s2/favicons?domain=mediapart.fr&sz=32"
              },
              title: "Emmanuel Macron saupoudre la Guyane de petites annonces, à distance de la population | Mediapart",
              article_snippet: "Sur la pêche, l’agriculture, l’orpaillage et l’évolution statutaire, Emmanuel Macron n’a pas rassuré une population globalement indifférente à des promesses qui ne résoudront pas les difficultés de l…",
              image_preview_link: "	https://pbs.twimg.com/card_img/1772903236879405056/uVQDcKXm?format=jpg&name=medium",
              safe: true,
            }
          ],
          trust: "Seems Legit",
          request_id: Date.now(),
          created_at: new Date(Date.now()),
          origin: {
            link: "https://twitter.com/home",
            name: "Twitter",
            favicon_link:
              "https://www.google.com/s2/favicons?domain=twitter.com&sz=32",
          },
        },
      ],
    })) as { historic: IConversation[] };
    console.log(storage_hist);
    historic = storage_hist.historic;
  });

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
    </section>
    <!-- <div class="howto" style="display: flex; padding-bottom: 1rem; align-items: space-between;">
    <img src="../../public/animation.gif" width="84" alt="" srcset="" />
    <span>Vous n'avez aucune recherche d'informations.... Surligner du texte et lancer la vérification.</span>
  </div> -->
    <div class="conv-list">
      {#each historic as convo}
        <Conversation {convo} />
      {/each}
    </div>
  </main>

  <footer></footer>
{/if}

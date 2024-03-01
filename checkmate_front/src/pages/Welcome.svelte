<script lang="ts">
  import { onMount } from "svelte";

  export let baseUrl;
  let isOnboarded: boolean | null = null;

  onMount(async () => {
    const data = await chrome.storage.local.get({ onboarded: false });
    isOnboarded = data.onboarded;
  });
</script>

{#if isOnboarded == null || !isOnboarded}
  <header>
    <div>
      <img src="../../public/icon.svg" width="32" alt="" srcset="" />
      CheckMate
    </div>
  </header>

  <main>
    <section>
      <img src="../../public/icon.svg" width="48" alt="" srcset="" />
      <h1>Bienvenue</h1>
      <span>Fact-checker n'importe quelle information sur le Web grâce à l'IA</span
      >
    </section>
    <div>
      <span>Comment utiliser:</span>
      <ul>
        <li>Surlignez le paragraphe que vous voulez vérifier</li>
        <li>Clic droit puis "Vérifier l'information"</li>
        <li>
          L'IA se charge de récolter des sources fiables et de les confronter
        </li>
        <li>CheckMate vous répond, avec les sources utilisées</li>
      </ul>
    </div>
    <button> Démarrer </button>
  </main>
{/if}

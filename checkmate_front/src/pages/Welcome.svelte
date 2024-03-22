<script lang="ts">
  import { onMount } from "svelte";
  import "../styles/welcome.css"
  import { onboarded } from "../stores";
  let isOnboarded: boolean | null = null;

  onMount(async () => {
    const data = await chrome.storage.local.get({ onboarded: false });
  });

  const handle_click = async () => {
    await chrome.storage.local.set({ onboarded: true });
    onboarded.set(true)
  } 
</script>

<main>
  <section>
    <img src="../../public/icon.svg" width="48" alt="" srcset="" />
    <h1>Bienvenue</h1>
    <span>Fact-checker n'importe quelle information sur le Web grâce à l'IA</span
    >
  </section>
  <div class="howto">
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
  <button on:click={() => handle_click()}> Démarrer </button>
</main>

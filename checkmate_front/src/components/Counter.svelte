<script lang="ts">
  import { onDestroy, onMount } from "svelte";

  export let count: number;
  let message: string | null = null;
  let information: string | null = null

  const increment = () => (count += 1);
  const decrement = () => (count -= 1);

  onMount(async () => {
    const info  = await chrome.storage.local.get();
    information = info.message
  })
</script>

<div>
  <p>
    {information!= null ? "Texte sélectionné : " : "Vous n'avez rien sélectionné"}
  </p>
  <div>
    {#if information}<span>{information}</span>{/if}
  </div>
</div>
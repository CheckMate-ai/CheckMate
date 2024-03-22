<script lang="ts">
  import { onMount } from "svelte";

  let message: string | null = null;
  let information: string | null = null;
  
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
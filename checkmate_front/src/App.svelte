<script lang="ts">
  import Welcome from "./pages/Welcome.svelte";
  import "./styles/welcome.css";
  import Home from "./pages/Home.svelte";
  import { onMount } from "svelte";
  import { onboarded } from "./stores";

  let showHome: boolean;

  onMount(async () => await chrome.storage.local.set({ onboarded: false }));
  let promise = (async () => {
    let res = await chrome.storage.local.get({ onboarded: false });
    onboarded.set(res.onboarded);
  })();

  onboarded.subscribe((value) => {
    showHome = value;
  });
</script>

<header>
  <img src="../../public/icon.svg" width="32" alt="" srcset="" />
  <h2>CheckMate</h2>
</header>
<div>
  {#await promise then isOnboarded}
    {#if showHome}
      <Home />
    {:else}
      <Welcome />
    {/if}
  {/await}
</div>

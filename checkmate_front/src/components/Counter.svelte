<script lang="ts">
  import { onDestroy, onMount } from "svelte";

  const BASE_URL = "http://localhost:8080/"

  let promise: Promise<string[]> = fetch_keywords()

  async function fetch_keywords() {
    const info  = await chrome.storage.local.get();
    const param = new URLSearchParams({
      question: info.message
    })
    const req = await fetch(BASE_URL+ "keyword?" +param)
    const data = await req.json()
    return data["result"]
  }

</script>
<div>
  {#await promise}
	<p>...waiting</p>
  {:then keywords}
  <ul>
    {#each keywords as keyword}
      <li>{keyword}</li>
    {/each}
  </ul>
  {:catch error}
    <p style="color: red">{error.message}</p>
  {/await}
</div>
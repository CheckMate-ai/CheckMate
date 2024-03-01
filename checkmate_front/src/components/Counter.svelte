<script lang="ts">
  export let count: number;
  let message: string | null = null;

  const increment = () => (count += 1);
  const decrement = () => (count -= 1);

  const handleSave = () => {
    chrome.storage.sync.set({ count }).then(() => {
      message = "Updated!";

      setTimeout(() => {
        message = null;
      }, 2000);
    });
  };
</script>

<div>
  <p>
    Current count: {count}
  </p>
  <div>
    <button on:click={decrement}>-</button>
    <button on:click={increment}>+</button>
    <button on:click={handleSave}>Save</button>
    {#if message}<span>{message}</span>{/if}
  </div>
</div>
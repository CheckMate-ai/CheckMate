import Counter from '../components/Counter.svelte';

const target: HTMLElement | null = document.getElementById('app');

async function render() {
  const { count } = await chrome.storage.sync.get({ count: 0 });

  if(target){
    new Counter({ target, props: { count } });
  }
}

document.addEventListener('DOMContentLoaded', render);

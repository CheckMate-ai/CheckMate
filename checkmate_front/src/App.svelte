<script>
  import { Router, Route } from "svelte-routing";
  import Welcome from "./pages/Welcome.svelte";
  import Counter from "./components/Counter.svelte"

  export let url = `${window.location}/`;
</script>

<Router {url}>
  <div>
    <Route path={url+"/home"} component={Counter} />
    <Route path={url+"/"}><Welcome baseUrl={url}/></Route>
  </div>
</Router>
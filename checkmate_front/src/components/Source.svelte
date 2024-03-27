<script lang="ts">
  import type { ISource } from "src/types/types";
  import "./source.css"
  const SNIPPET_MAX_LENGTH = 110;
  const LINK_MAX_LENGTH = 48;
  export let source: ISource
</script>
<a class="article" target="_blank" href={source.website.link}>
  <article>
    <img src={source.image_preview_link} alt="">
    <div>
      <span class="title">{source.title.length <= 78 ? source.title : source.title.substring(0, 75) + "..."}</span>
      <p>{source.article_snippet.length <= SNIPPET_MAX_LENGTH ? source.article_snippet : source.article_snippet.substring(0, SNIPPET_MAX_LENGTH-3) + "..."}</p>
      <div class="link"><img src={source.website.favicon_link} alt=""><span>{source.website.link.length <= LINK_MAX_LENGTH ? source.website.link : source.website.link.substring(0, LINK_MAX_LENGTH-3) + "..."}</span></div>
    </div>
  </article>
</a>
<script lang="ts">
  import "./styles/welcome.css";
  import Source from "./components/Source.svelte";
  import {
    Trust,
    type IConversation,
    type IWebsite,
    type Message,
    type ISource,
  } from "./types/types";
  import Conversation from "./components/Conversation.svelte";

  let promise_conv: Promise<IConversation> = check();
  const example: IConversation = {
    title:
      "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Harum quos voluptas dolorem quis ex veritatis debitis nobis necessitatibus mollitia excepturi vel ea itaque officiis iusto adipisci ipsa, nam odio perspiciatis!",
    messages: [
      {
        content:
          "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Harum quos voluptas dolorem quis ex veritatis debitis nobis necessitatibus mollitia excepturi vel ea itaque officiis iusto adipisci ipsa, nam odio perspiciatis!",
        from: "User",
      },
      {
        content:
          "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Harum quos voluptas dolorem quis ex veritatis debitis nobis necessitatibus mollitia excepturi vel ea itaque officiis iusto adipisci ipsa, nam odio perspiciatis!",
        from: "AI",
      },
      {
        id: "1",
        website: {
          name: "Mediapart",
          link: "https://www.mediapart.fr/journal/france/270324/emmanuel-macron-saupoudre-la-guyane-de-petites-annonces-distance-de-la-population",
          favicon_link:
            "https://www.google.com/s2/favicons?domain=mediapart.fr&sz=32",
        },
        title:
          "Emmanuel Macron saupoudre la Guyane de petites annonces, à distance de la population | Mediapart",
        article_snippet:
          "Sur la pêche, l’agriculture, l’orpaillage et l’évolution statutaire, Emmanuel Macron n’a pas rassuré une population globalement indifférente à des promesses qui ne résoudront pas les difficultés de l…",
        image_preview_link:
          "	https://pbs.twimg.com/card_img/1772903236879405056/uVQDcKXm?format=jpg&name=medium",
        safe: true,
      },
    ],
    trust: Trust.Legit,
    request_id: Date.now(),
    created_at: new Date(Date.now()),
    origin: {
      link: "https://twitter.com/home",
      name: "Twitter",
      favicon_link:
        "https://www.google.com/s2/favicons?domain=twitter.com&sz=32",
    },
  };

  const index_to_trust = [Trust.Legit, Trust.Fake, Trust.Doubtful];

  const example_sources = {
    sources: [
      {
        date: "2023-05-14",
        website: {
          favicon_link:
            "https://www.google.com/s2/favicons?domain=www.lemonde.fr",
          link: "https://www.lemonde.fr/en/m-le-mag/article/2023/05/14/bologna-italian-life-the-anti-meloni-way_6026625_117.html",
          name: "Le Monde.fr",
        },
        safe: true,
        article_snippet:
          "May 14, 2023 ... ... open and collective alternative. A compass ... Their two keywords: Autonomy and anonymity. ... In recent months, its activists have blocked the ...",
        id: 0,
        title: "Bologna, Italian life the anti-Meloni way",
        image_preview_link:
          "https://img.lemde.fr/2023/05/02/384/0/1099/732/1440/960/60/0/b9471f6_281807-3320134.jpg",
      },
    ],
    question:
      "Pour montrer son ouverture d’esprit, la Russie envoie 30 000 homosexuels sur le front ukrainien",
    keywords: [
      "Russia",
      "open-mindedness",
      "homosexuals",
      "Ukraine front",
      "keywords",
    ],
  };

  const example_response = {
    sentence:
      "Pour montrer son ouverture d’esprit, la Russie envoie 30 000 homosexuels sur le front ukrainien",
    sources: [
      "May 14, 2023 ... ... open and collective alternative. A compass ... Their two keywords: Autonomy and anonymity. ... In recent months, its activists have blocked the ...",
    ],
    label: 2,
    explanation:
      'Hello. Based on the provided source, it does not seem to mention anything about Russia sending 30,000 homosexuals to the Ukrainian front. Therefore, I cannot verify this statement as true. My output is: {"result" : "Unsure"}',
  };

  async function check() {
    let info = await chrome.storage.local.get({ message: "vide", origin: {} });
    console.log(info.message);
    console.log(JSON.parse(info.origin));
    let req = await fetch(
      "http://localhost:8080/getSources?question=" + info.message
    );
    const sources = await req.json();
    req = await fetch(
      "http://localhost:8080/getAdvice?question=" + info.message
    );
    const response = await req.json();
    /*     const response = example_response;
    const sources = example_sources; */

    const typed_sources = sources.sources.map((source: any, i: number) => {
      const src: ISource = {
        website: source.website as IWebsite,
        safe: source.safe,
        image_preview_link: source.image_preview_link,
        id: i.toString(),
        article_snippet: source.article_snippet,
        title: source.title,
      };
      if (source.date != "NO DATE") src.date = new Date(source.date);
      return src;
    });
    const messages: Message[] = [
      {
        content: info.message,
        from: "User",
      },
      {
        content: response.explanation,
        from: "AI",
      },
    ];
    const res: IConversation = {
      title: sources.keywords.join(","),
      origin: JSON.parse(info.origin) as IWebsite,
      trust: index_to_trust[response.label],
      request_id: Date.now(),
      created_at: new Date(Date.now()),
      messages: messages.concat(typed_sources),
    };
    // console.log(sources);
    let {history} = await chrome.storage.local.get({ history: []})
    if(!history.some((conv: any) => conv.title==res.title)){
      chrome.storage.local.set({ history: [res].concat(history)})
    }
    return res;
  }
</script>

<header>
  <img src="../../public/icon.svg" width="32" alt="" srcset="" />
  <h2>CheckMate</h2>
</header>
<main>
  <section>
    <h1>Checking</h1>
  </section>
  {#await promise_conv}
    Loading...
  {:then conversation}
    <Conversation convo={conversation} detail_view={true} />
  {:catch error}
    {error}
  {/await}
</main>

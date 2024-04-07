# CheckMate
Use the power of AI to verify easily online information with our Chrome Extension

# Backend

We are using Kotlin for the backend. In particular, Spring framework to build the server. Gardle is used for build automation.

How to start the server?
```
cd backend/server
./gradlew build
./gradlew bootRun
```


# Frontend

We are using Svelte coupled with Typescript for the frontend. Vite.js is used as our developement tooling system. The version `v21.6.2` of Node has been used for this project.

How to start the client?

```
cd checkmate_front
npm install
npm run dev
```

Once it's done, a folder `/dist` should appear. In your browser, for instance Google Chrome : 
- Go to chrome://extensions 
- Enable de developer mode if disabled
- Click on "Load unpacked" (*Charger l'extension non empactée*)
- Select the `/dist` directory of the project
- Optional : Pin it to your browser for seamless experience


# Contributors
- DIBASSI Brahima
- FAZAZI Zeid
- FRANCOISE Raphaël
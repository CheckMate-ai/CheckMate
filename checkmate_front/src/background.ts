import type { IWebsite } from "./types/types"

chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.removeAll()

  chrome.contextMenus.create({
    id: "checking-" + Date.now().toString(),
    contexts: ["selection"],
    title: "Vérifier l'information"
  })
})

chrome.contextMenus.onClicked.addListener(async (info, tab) => {
  if (info.menuItemId.toString().startsWith("checking-")) {
    const origin = await get_current_tab()
    await chrome.storage.local.set({ message: info.selectionText, origin: JSON.stringify(origin) })
    chrome.windows.create({
      url: "./src/popup/checking.html",
      type: "popup",
      top: 150,
      left: 1200,
      width: 400,
      height: 600
    });
  }
})

async function get_current_tab() {
  let queryOptions = { active: true, currentWindow: true };
  let [tab] = await chrome.tabs.query(queryOptions);
  const res: IWebsite = {
    favicon_link: tab.favIconUrl ?? "",
    link: tab.url ?? "",
    name: tab.title ?? ""
  }
  return res;
}
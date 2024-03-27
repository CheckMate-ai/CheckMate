chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.removeAll()

  chrome.contextMenus.create({
    id: "checking-" + Date.now().toString(),
    contexts: ["selection"],
    title: "Vérifier l'information"
  })
})

chrome.contextMenus.onClicked.addListener(async (info, tab) => {
  if(info.menuItemId.toString().startsWith("checking-")){
    await chrome.storage.local.set({ message: info.selectionText})
    chrome.windows.create({
      url:"./src/popup/checking.html", 
      type: "popup",
      top: 150,
      left: 1200,
      width: 300,
      height: 300
    });
  }
  return true
})
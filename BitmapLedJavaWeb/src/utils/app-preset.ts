import disableDevtool from 'disable-devtool'

const dev = import.meta.env.VITE_APP_ENV == 'dev'
if (!dev) {
  disableDevtool()
}

window.addEventListener('contextmenu', (e: MouseEvent) => {
  const tagName = e.target?.tagName
  if (tagName == 'input' || tagName == 'textarea') return
  e.preventDefault()
})

document.querySelector('#app').addEventListener('wheel', (e: MouseEvent) => {
  if (e.ctrlKey || e.metaKey) e.preventDefault()
})

window.addEventListener('DOMMouseScroll', (e: MouseEvent) => {
  if (e.ctrlKey || e.metaKey) e.preventDefault()
})

window.addEventListener(
  'keydown',
  (e: KeyboardEvent) => {
    if (!e.ctrlKey && !e.metaKey) return
    if ('=+-ps'.includes(e.key)) {
      e.preventDefault()
    }
  },
  false
)

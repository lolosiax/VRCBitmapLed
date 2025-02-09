/*fix the import warning issue of vue file*/
declare module '*.vue' {
  import { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module '*.svg' {
  const obj: string
  export default obj
}

declare module '*.png' {
  const obj: string
  export default obj
}

declare module '*.scss' {
  const classes: { readonly [key: string]: string }
  export default classes
}

declare module '*?raw' {
  const obj: string
  export default obj
}

declare module '*?url' {
  const obj: string
  export default obj
}

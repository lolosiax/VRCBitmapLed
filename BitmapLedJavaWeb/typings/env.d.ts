declare global {
  interface ImportMetaEnv {
    readonly VITE_APP_BASE_MODE: 'local' | 'url' | 'port'
    readonly VITE_APP_BASE_URL: string
    readonly VITE_APP_BASE_PORT: number
    readonly VITE_APP_ENV: 'prod' | 'test' | 'dev'
    readonly VITE_BUILD_TIMESTAMP: string
    readonly VITE_BUILD_DISPLAY_NAME: string
    readonly VITE_GIT_COMMIT: string
    readonly PROJECT_VERSION: string // 前端版本
    readonly VITE_PROJECT_VERSION: string // 后端版本
    // 更多环境变量...
  }
  interface ImportMeta {
    readonly env: ImportMetaEnv
  }

  interface Window {
    readonly NGINX_BASE_URL: string | undefined
  }
}
export {}

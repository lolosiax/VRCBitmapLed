export interface CountAll<T> {
  rows: T[]
  count: number
}

export interface BasicModel {
  createdAt: string
  createdBy: string
  updatedAt: string
  updatedBy: string
  deleted: boolean
}

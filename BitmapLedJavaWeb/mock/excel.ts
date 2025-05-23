import Mock from 'mockjs'

const NameList: any = []
const count = 100

for (let i = 0; i < count; i++) {
  NameList.push(
    Mock.mock({
      name: '@first'
    })
  )
}
NameList.push({ name: 'mock-Pan' })

export default [
  // username search
  {
    url: '/vue3-admin-plus/search/user',
    method: 'get',
    response: (config) => {
      const { name } = config.query
      const mockNameList = NameList.filter((item) => {
        // @ts-ignore
        const lowerCaseName = item.name.toLowerCase()
        return !(name && !lowerCaseName.includes(name.toLowerCase()))
      })
      return {
        code: 20000,
        data: { items: mockNameList }
      }
    }
  },

  // transaction list
  {
    url: '/vue3-admin-plus/transaction/list',
    method: 'get',
    response: () => {
      return {
        code: 20000,
        data: {
          total: 20,
          'items|20': [
            {
              order_no: '@guid()',
              timestamp: +Mock.Random.date('T'),
              userName: '@name()',
              price: '@float(1000, 15000, 0, 2)',
              'status|1': ['success', 'pending']
            }
          ]
        }
      }
    }
  }
]

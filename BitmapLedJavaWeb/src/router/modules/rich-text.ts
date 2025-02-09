import Layout from '@/layout/default/index.vue'
const richText = {
  path: '/rich-text',
  component: Layout,
  meta: { title: 'Rich Text', icon: 'clipboard' },
  alwaysShow: true,
  children: [
    {
      path: 'tinymce',
      name: 'Tinymce',
      component: () => import('@/views/rich-text/TinymceExample.vue'),
      meta: { title: 'Tinymce', icon: 'nested' }
    }
  ]
}

export default richText

import { shallowMount } from '@vue/test-utils';
import FindabilityComponent from '@/views/findability-component/FindabilityComponent'

describe('FindabilityComponent.vue', () => {
  it('should render Create User Button', () => {
    const wrapper = shallowMount(FindabilityComponent);
    const contentButton = wrapper.find('button');
    expect(contentButton.text()).toEqual('Save metadata');
  })
})

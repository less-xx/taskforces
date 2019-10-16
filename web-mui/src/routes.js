import TaskforceGroups from './components/TaskforceGroups';
import Taskforces from './components/Taskforces';
import Content from './Content';

export const routes = [
    {
        exact: true,
        path: '/',
        content: Content
    },
    {
        exact: true,
        path: '/taskforce-groups',
        content: TaskforceGroups
    },
    {
        path: '/taskforce-groups/:groupId',
        content: Taskforces
    }
]
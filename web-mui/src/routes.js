import TaskforceGroups from './components/TaskforceGroups';
import Content from './Content';

export const routes = [
    {
        exact: true,
        path: '/',
        content: Content
    },
    {
        path: '/taskforces',
        content: TaskforceGroups
    }
]
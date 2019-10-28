import TaskforceGroups from './components/TaskforceGroups';
import Taskforces from './components/Taskforces';
import TaskforceBuilder from './components/TaskforceBuilder';
import Content from './Content';
import UserDefinedFilePaths from './components/UserDefinedFilePaths';

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
        exact: true,
        path: '/taskforce-groups/:groupId',
        content: Taskforces
    },
    {
        exact: true,
        path: '/taskforce-builder',
        content: TaskforceBuilder
    },
    {
        exact: true,
        path: '/resource/user-defined-file-paths',
        content: UserDefinedFilePaths
    },
]
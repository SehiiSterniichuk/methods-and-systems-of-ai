export type Question = {
    id: number,
    name: string,
    condition: string
}

export const fakeQuestions:Question[] = [
    {
        id: -1,
        condition: "How are you? odio rem sapiente voluptate? Asperiores ex exercitationem, fuga itaque labore molestiae mollitia possimus reiciendis sapiente similique tempore.",
        name: "Check mood odio rem sapiente voluptate? Asperiores ex exercitationem, fuga itaque labore molestiae mollitia possimus reiciendis sapiente similique tempore."
    },
    {
        id: -2,
        condition: "How old are you? odio rem sapiente voluptate? Asperiores ex exercitationem, fuga itaque labore molestiae mollitia possimus reiciendis sapiente similique tempore.",
        name: "Check age odio rem sapiente voluptate? Asperiores ex exercitationem, fuga itaque labore molestiae mollitia possimus reiciendis sapiente similique tempore."
    }
]
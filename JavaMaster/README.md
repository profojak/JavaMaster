Zdrojove kody vypracovane na cviceni.

Stahnuti repozitare:
`git clone git@gitlab.fel.cvut.cz:szadkrud/pjv2020_friday.git`

V **master** branchi bude vzdy kod ze ktereho si studenti budou vytvaret vlastni
vetve.
Tudiz do **master branche necommitujte**!

Pro **vytvoreni vlastni vetve** postupujte nasledovne:

1) Aktualizovat lokalni obraz repozitare:
`git fetch` (pripadne `git pull`)
2) Zalozit novou *lokalni* vetev `git checkout -b username_cv03`
(*username* nahradte svym CVUT loginem a *_cv03* cislem aktualniho cviceni)
3) Vytvorit vetev na serveru `git push --set-upstream origin username_cv03` 
(aby cvicici mohl konstruktivne jizlive komentovat vas kod :) )
4) Commitovat `git commit -a -m"new branch username_cv03"` 
(option `-a` nahrazuje `git add` ktery se pred commitem musi delat)
5) Uploadovat zmenu `git push`

V prubehu hodiny uz pak staci opakovat krok 4) a 5), jen prosim do **zpravy
commitu napiste o tom co commitujete** (napr.: "implementace tridy Car" nebo 
"prvni cast cviceni").

Problemy s Gitem:
>  remote: GitLab: You are not allowed to push code to protected branches on this project.

Nejspis pushujes do master branche (kam nemas pravo). To je kvuli tomu
ze sis neprepnul branche.
Pro zjisteni aktivni (HEAD) branche dej `git branche` kde krizek indikuje aktivni branche.
Pro prepnuti branche zadej `git checkout username_cv03`.
Pokud jeste neexistuje postupuj podle navodu pro vytvoreni vlastni branche vyse.

>   Windows : Problem: je zapotrebi opakovane nastavovat SSH klic

https://stackoverflow.com/questions/18404272/running-ssh-agent-when-starting-git-bash-on-windows



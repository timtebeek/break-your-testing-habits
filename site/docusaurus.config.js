// @ts-check
// `@type` JSDoc annotations allow editor autocompletion and type checking
// (when paired with `@ts-check`).
// There are various equivalent ways to declare your Docusaurus config.
// See: https://docusaurus.io/docs/api/docusaurus-config

import {themes as prismThemes} from 'prism-react-renderer';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Break your testing habits',
  tagline: 'Adopt better testing practices with automated refactoring',
  favicon: 'img/favicon.ico',

  // Future flags, see https://docusaurus.io/docs/api/docusaurus-config#future
  future: {
    v4: true, // Improve compatibility with the upcoming Docusaurus v4
  },

  // Set the production url of your site here
  url: 'https://timtebeek.github.io/',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  // baseUrl: '/',
  baseUrl: '/break-your-testing-habits/',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'timtebeek', // Usually your GitHub org/user name.
  projectName: 'break-your-testing-habits', // Usually your repo name.

  onBrokenLinks: 'throw',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: './sidebars.js',
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          editUrl:
            'https://github.com/timtebeek/break-your-testing-habits/tree/main/site/',
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      // Replace with your project's social card
      image: 'img/social-card.jpg',
      colorMode: {
        respectPrefersColorScheme: true,
      },
      navbar: {
        title: 'Break your testing habits',
        logo: {
          alt: 'Logo',
          src: 'img/rewrite-logo.png',
        },
        items: [
          {
            type: 'docSidebar',
            sidebarId: 'tutorialSidebar',
            position: 'left',
            label: 'Tutorial',
          },
          {
            href: 'https://github.com/timtebeek/break-your-testing-habits',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'light',
        links: [
          {
            title: 'Docs',
            items: [
              {
                  label: 'OpenRewrite',
                  href: 'https://docs.openrewrite.org/recipes/java/testing/assertj/assertj-best-practices',
              },
              {
                  label: 'Moderne',
                  href: 'https://docs.moderne.io/',
              },
              {
                  label: 'Error Prone Support',
                  href: 'https://error-prone.picnic.tech/refasterrules/AssertJArrayRules/',
              },
            ],
          },
          {
            title: 'Socials',
            items: [
                {
                    label: 'OpenRewrite',
                    href: 'https://bsky.app/profile/openrewrite.github.io',
                },
                {
                    label: 'Moderne',
                    href: 'https://bsky.app/profile/did:plc:vnhmeu5qj3w4rdoagg4pan7g',
                },
                {
                    label: 'Tim',
                    href: 'https://bsky.app/profile/timtebeek.github.io',
                },
                {
                    label: 'Rick',
                    href: 'https://bsky.app/profile/did:plc:3tp5547p3bgmzq3n6v6dad4f',
                },
            ],
          },
        ],
        copyright: `Copyright © ${new Date().getFullYear()} Moderne, Inc.`,
      },
      prism: {
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
        additionalLanguages: [
            "bash",
            "java",
        ],
      },
    }),
};

export default config;

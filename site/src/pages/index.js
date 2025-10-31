import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';

import Heading from '@theme/Heading';
import styles from './index.module.css';

function HomepageHeader() {
  const {siteConfig} = useDocusaurusContext();
  return (
    <header className={clsx('hero hero--primary', styles.heroBanner)}>
      <div className="container">
        <Heading as="h1" className="hero__title">
          {siteConfig.title}
        </Heading>
        <p className="hero__subtitle">{siteConfig.tagline}</p>
        <p className="hero__subtitle">A workshop by Rick Ossendrijver and Tim te Beek.</p>
        <div className={styles.buttons}>
          <Link
            className="button button--secondary button--lg"
            to="/docs/intro">
            Get started
          </Link>
        </div>
        <img src={siteConfig.themeConfig.image} style={{ marginTop: '2rem', maxWidth: '750px', width: '100%' }}  alt={'Tim and Rick'}/>
      </div>
    </header>
  );
}

export default function Home() {
  const {siteConfig} = useDocusaurusContext();
  return (
    <Layout
      title={`${siteConfig.title}`}
      description="Adopt better testing practices with automated refactoring.">
      <HomepageHeader />
      <main>
      </main>
    </Layout>
  );
}

  


<!DOCTYPE html>
<html>
  <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# githubog: http://ogp.me/ns/fb/githubog#">
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>UrbanGame/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java at android · blstream/UrbanGame</title>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub" />
    <link rel="fluid-icon" href="https://github.com/fluidicon.png" title="GitHub" />
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-114.png" />
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114.png" />
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-144.png" />
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144.png" />
    <link rel="logo" type="image/svg" href="http://github-media-downloads.s3.amazonaws.com/github-logo.svg" />
    <link rel="xhr-socket" href="/_sockets" />


    <meta name="msapplication-TileImage" content="/windows-tile.png" />
    <meta name="msapplication-TileColor" content="#ffffff" />
    <meta name="selected-link" value="repo_source" data-pjax-transient />
    <meta content="collector.githubapp.com" name="octolytics-host" /><meta content="github" name="octolytics-app-id" /><meta content="4090708" name="octolytics-actor-id" /><meta content="891d9696f3c83fc94ae1ce92d7a683843d6c332deb6a95b4e5ed2e8741241826" name="octolytics-actor-hash" />

    
    
    <link rel="icon" type="image/x-icon" href="/favicon.ico" />

    <meta content="authenticity_token" name="csrf-param" />
<meta content="yUFYkIxmHiKZu2/mHwXORKWYuOnoKRGJhgUys+zETxI=" name="csrf-token" />

    <link href="https://a248.e.akamai.net/assets.github.com/assets/github-d63f89e307e2e357d3b7160b3cd4020463f9bbc1.css" media="all" rel="stylesheet" type="text/css" />
    <link href="https://a248.e.akamai.net/assets.github.com/assets/github2-4a2696ec075bd8d27843df00793c7e9d6525dded.css" media="all" rel="stylesheet" type="text/css" />
    


      <script src="https://a248.e.akamai.net/assets.github.com/assets/frameworks-92d138f450f2960501e28397a2f63b0f100590f0.js" type="text/javascript"></script>
      <script src="https://a248.e.akamai.net/assets.github.com/assets/github-60bb3beedc339be272bd2acfce1cae3b79371737.js" type="text/javascript"></script>
      
      <meta http-equiv="x-pjax-version" content="7159fafc2fc92e02281814323bde3687">

        <link data-pjax-transient rel='permalink' href='/blstream/UrbanGame/blob/8909860097313d5da757bb479934f230843cae4e/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java'>
    <meta property="og:title" content="UrbanGame"/>
    <meta property="og:type" content="githubog:gitrepository"/>
    <meta property="og:url" content="https://github.com/blstream/UrbanGame"/>
    <meta property="og:image" content="https://secure.gravatar.com/avatar/2b24f858dbbdec315538a23a4e3caeab?s=420&amp;d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png"/>
    <meta property="og:site_name" content="GitHub"/>
    <meta property="og:description" content="UrbanGame - Urban Game is a system (mobile and web applications) designed to support game organizer and contributors. It is made under Patronage 2013 Wrocław Edition and licensed on Apache License 2.0. Check below link for details."/>
    <meta property="twitter:card" content="summary"/>
    <meta property="twitter:site" content="@GitHub">
    <meta property="twitter:title" content="blstream/UrbanGame"/>

    <meta name="description" content="UrbanGame - Urban Game is a system (mobile and web applications) designed to support game organizer and contributors. It is made under Patronage 2013 Wrocław Edition and licensed on Apache License 2.0. Check below link for details." />


    <meta content="316451" name="octolytics-dimension-user_id" /><meta content="7162504" name="octolytics-dimension-repository_id" />
  <link href="https://github.com/blstream/UrbanGame/commits/android.atom" rel="alternate" title="Recent Commits to UrbanGame:android" type="application/atom+xml" />

  </head>


  <body class="logged_in page-blob linux vis-public env-production  ">
    <div id="wrapper">

      

      
      
      

      <div class="header header-logged-in true">
  <div class="container clearfix">

    <a class="header-logo-invertocat" href="https://github.com/">
  <span class="mega-octicon octicon-mark-github"></span>
</a>

    <div class="divider-vertical"></div>

      <a href="/blstream/UrbanGame/notifications" class="notification-indicator tooltipped downwards contextually-unread" title="You have unread notifications in this repository">
    <span class="mail-status unread"></span>
  </a>
  <div class="divider-vertical"></div>


      <div class="command-bar js-command-bar  in-repository">
          <form accept-charset="UTF-8" action="/search" class="command-bar-form" id="top_search_form" method="get">
  <a href="/search/advanced" class="advanced-search-icon tooltipped downwards command-bar-search" id="advanced_search" title="Advanced search"><span class="octicon octicon-gear "></span></a>

  <input type="text" data-hotkey="/ s" name="q" id="js-command-bar-field" placeholder="Search or type a command" tabindex="1" autocapitalize="off"
    data-username="msfblstream"
      data-repo="blstream/UrbanGame"
      data-branch="android"
      data-sha="85ddf3573624980798d0b41f6dd1e0c8d2052e10"
  >

    <input type="hidden" name="nwo" value="blstream/UrbanGame" />

    <div class="select-menu js-menu-container js-select-menu search-context-select-menu">
      <span class="minibutton select-menu-button js-menu-target">
        <span class="js-select-button">This repository</span>
      </span>

      <div class="select-menu-modal-holder js-menu-content js-navigation-container">
        <div class="select-menu-modal">

          <div class="select-menu-item js-navigation-item selected">
            <span class="select-menu-item-icon octicon octicon-check"></span>
            <input type="radio" class="js-search-this-repository" name="search_target" value="repository" checked="checked" />
            <div class="select-menu-item-text js-select-button-text">This repository</div>
          </div> <!-- /.select-menu-item -->

          <div class="select-menu-item js-navigation-item">
            <span class="select-menu-item-icon octicon octicon-check"></span>
            <input type="radio" name="search_target" value="global" />
            <div class="select-menu-item-text js-select-button-text">All repositories</div>
          </div> <!-- /.select-menu-item -->

        </div>
      </div>
    </div>

  <span class="octicon help tooltipped downwards" title="Show command bar help">
    <span class="octicon octicon-question"></span>
  </span>


  <input type="hidden" name="ref" value="cmdform">

  <div class="divider-vertical"></div>

</form>
        <ul class="top-nav">
            <li class="explore"><a href="https://github.com/explore">Explore</a></li>
            <li><a href="https://gist.github.com">Gist</a></li>
            <li><a href="/blog">Blog</a></li>
          <li><a href="http://help.github.com">Help</a></li>
        </ul>
      </div>

    

  

    <ul id="user-links">
      <li>
        <a href="https://github.com/msfblstream" class="name">
          <img height="20" src="https://secure.gravatar.com/avatar/f71dd6cd8fe8160ed62b9ad3b24a6024?s=140&amp;d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png" width="20" /> msfblstream
        </a>
      </li>

        <li>
          <a href="/new" id="new_repo" class="tooltipped downwards" title="Create a new repo">
            <span class="octicon octicon-repo-create"></span>
          </a>
        </li>

        <li>
          <a href="/settings/profile" id="account_settings"
            class="tooltipped downwards"
            title="Account settings (You have no verified emails)">
            <span class="octicon octicon-tools"></span>
          </a>
            <span class="settings-warning">!</span>
        </li>
        <li>
          <a class="tooltipped downwards" href="/logout" data-method="post" id="logout" title="Sign out">
            <span class="octicon octicon-log-out"></span>
          </a>
        </li>

    </ul>


<div class="js-new-dropdown-contents hidden">
  <ul class="dropdown-menu">
    <li>
      <a href="/new"><span class="octicon octicon-repo-create"></span> New repository</a>
    </li>
    <li>
        <a href="https://github.com/blstream/UrbanGame/issues/new"><span class="octicon octicon-issue-opened"></span> New issue</a>
    </li>
    <li>
    </li>
    <li>
      <a href="/organizations/new"><span class="octicon octicon-list-unordered"></span> New organization</a>
    </li>
  </ul>
</div>


    
  </div>
</div>

      

      <div class="global-notice warn"><div class="global-notice-inner"><h2>You don't have any verified emails.  We recommend <a href="https://github.com/settings/emails">verifying</a> at least one email.</h2><p>Email verification will help our support team help you in case you have any email issues or lose your password.</p></div></div>

      


            <div class="site hfeed" itemscope itemtype="http://schema.org/WebPage">
      <div class="hentry">
        
        <div class="pagehead repohead instapaper_ignore readability-menu ">
          <div class="container">
            <div class="title-actions-bar">
              

<ul class="pagehead-actions">

    <li class="nspr">
      <a href="/blstream/UrbanGame/pull/new/android" class="button minibutton btn-pull-request" icon_class="octicon-git-pull-request"><span class="octicon octicon-git-pull-request"></span>Pull Request</a>
    </li>

    <li class="subscription">
      <form accept-charset="UTF-8" action="/notifications/subscribe" data-autosubmit="true" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="authenticity_token" type="hidden" value="yUFYkIxmHiKZu2/mHwXORKWYuOnoKRGJhgUys+zETxI=" /></div>  <input id="repository_id" name="repository_id" type="hidden" value="7162504" />

    <div class="select-menu js-menu-container js-select-menu">
      <span class="minibutton select-menu-button js-menu-target">
        <span class="js-select-button">
          <span class="octicon octicon-eye-unwatch"></span>
          Unwatch
        </span>
      </span>

      <div class="select-menu-modal-holder js-menu-content">
        <div class="select-menu-modal">
          <div class="select-menu-header">
            <span class="select-menu-title">Notification status</span>
            <span class="octicon octicon-remove-close js-menu-close"></span>
          </div> <!-- /.select-menu-header -->

          <div class="select-menu-list js-navigation-container">

            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <div class="select-menu-item-text">
                <input id="do_included" name="do" type="radio" value="included" />
                <h4>Not watching</h4>
                <span class="description">You only receive notifications for discussions in which you participate or are @mentioned.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="octicon octicon-eye-watch"></span>
                  Watch
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

            <div class="select-menu-item js-navigation-item selected">
              <span class="select-menu-item-icon octicon octicon octicon-check"></span>
              <div class="select-menu-item-text">
                <input checked="checked" id="do_subscribed" name="do" type="radio" value="subscribed" />
                <h4>Watching</h4>
                <span class="description">You receive notifications for all discussions in this repository.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="octicon octicon-eye-unwatch"></span>
                  Unwatch
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <div class="select-menu-item-text">
                <input id="do_ignore" name="do" type="radio" value="ignore" />
                <h4>Ignoring</h4>
                <span class="description">You do not receive any notifications for discussions in this repository.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="octicon octicon-mute"></span>
                  Stop ignoring
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

          </div> <!-- /.select-menu-list -->

        </div> <!-- /.select-menu-modal -->
      </div> <!-- /.select-menu-modal-holder -->
    </div> <!-- /.select-menu -->

</form>
    </li>

    <li class="js-toggler-container js-social-container starring-container ">
      <a href="/blstream/UrbanGame/unstar" class="minibutton js-toggler-target star-button starred upwards" title="Unstar this repo" data-remote="true" data-method="post" rel="nofollow">
        <span class="octicon octicon-star-delete"></span>
        <span class="text">Unstar</span>
      </a>
      <a href="/blstream/UrbanGame/star" class="minibutton js-toggler-target star-button unstarred upwards" title="Star this repo" data-remote="true" data-method="post" rel="nofollow">
        <span class="octicon octicon-star"></span>
        <span class="text">Star</span>
      </a>
      <a class="social-count js-social-count" href="/blstream/UrbanGame/stargazers">0</a>
    </li>

        <li>
          <a href="/blstream/UrbanGame/fork" class="minibutton js-toggler-target fork-button lighter upwards" title="Fork this repo" rel="facebox nofollow">
            <span class="octicon octicon-git-branch-create"></span>
            <span class="text">Fork</span>
          </a>
          <a href="/blstream/UrbanGame/network" class="social-count">21</a>
        </li>


</ul>

              <h1 itemscope itemtype="http://data-vocabulary.org/Breadcrumb" class="entry-title public">
                <span class="repo-label"><span>public</span></span>
                <span class="mega-octicon octicon-repo"></span>
                <span class="author vcard">
                  <a href="/blstream" class="url fn" itemprop="url" rel="author">
                  <span itemprop="title">blstream</span>
                  </a></span> /
                <strong><a href="/blstream/UrbanGame" class="js-current-repository">UrbanGame</a></strong>
              </h1>
            </div>

            
  <ul class="tabs">
    <li class="pulse-nav"><a href="/blstream/UrbanGame/pulse" class="js-selected-navigation-item " data-selected-links="pulse /blstream/UrbanGame/pulse" rel="nofollow"><span class="octicon octicon-pulse"></span></a></li>
    <li><a href="/blstream/UrbanGame/tree/android" class="js-selected-navigation-item selected" data-selected-links="repo_source repo_downloads repo_commits repo_tags repo_branches /blstream/UrbanGame/tree/android">Code</a></li>
    <li><a href="/blstream/UrbanGame/network" class="js-selected-navigation-item " data-selected-links="repo_network /blstream/UrbanGame/network">Network</a></li>
    <li><a href="/blstream/UrbanGame/pulls" class="js-selected-navigation-item " data-selected-links="repo_pulls /blstream/UrbanGame/pulls">Pull Requests <span class='counter'>4</span></a></li>


      <li><a href="/blstream/UrbanGame/wiki" class="js-selected-navigation-item " data-selected-links="repo_wiki /blstream/UrbanGame/wiki">Wiki</a></li>


    <li><a href="/blstream/UrbanGame/graphs" class="js-selected-navigation-item " data-selected-links="repo_graphs repo_contributors /blstream/UrbanGame/graphs">Graphs</a></li>


  </ul>
  
<div class="tabnav">

  <span class="tabnav-right">
    <ul class="tabnav-tabs">
          <li><a href="/blstream/UrbanGame/tags" class="js-selected-navigation-item tabnav-tab" data-selected-links="repo_tags /blstream/UrbanGame/tags">Tags <span class="counter blank">0</span></a></li>
    </ul>
  </span>

  <div class="tabnav-widget scope">


    <div class="select-menu js-menu-container js-select-menu js-branch-menu">
      <a class="minibutton select-menu-button js-menu-target" data-hotkey="w" data-ref="android">
        <span class="octicon octicon-branch"></span>
        <i>branch:</i>
        <span class="js-select-button">android</span>
      </a>

      <div class="select-menu-modal-holder js-menu-content js-navigation-container">

        <div class="select-menu-modal">
          <div class="select-menu-header">
            <span class="select-menu-title">Switch branches/tags</span>
            <span class="octicon octicon-remove-close js-menu-close"></span>
          </div> <!-- /.select-menu-header -->

          <div class="select-menu-filters">
            <div class="select-menu-text-filter">
              <input type="text" id="commitish-filter-field" class="js-filterable-field js-navigation-enable" placeholder="Find or create a branch…">
            </div>
            <div class="select-menu-tabs">
              <ul>
                <li class="select-menu-tab">
                  <a href="#" data-tab-filter="branches" class="js-select-menu-tab">Branches</a>
                </li>
                <li class="select-menu-tab">
                  <a href="#" data-tab-filter="tags" class="js-select-menu-tab">Tags</a>
                </li>
              </ul>
            </div><!-- /.select-menu-tabs -->
          </div><!-- /.select-menu-filters -->

          <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket css-truncate" data-tab-filter="branches">

            <div data-filterable-for="commitish-filter-field" data-filterable-type="substring">

                <div class="select-menu-item js-navigation-item selected">
                  <span class="select-menu-item-icon octicon octicon-check"></span>
                  <a href="/blstream/UrbanGame/blob/android/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="android" rel="nofollow" title="android">android</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item ">
                  <span class="select-menu-item-icon octicon octicon-check"></span>
                  <a href="/blstream/UrbanGame/blob/gh-pages/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="gh-pages" rel="nofollow" title="gh-pages">gh-pages</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item ">
                  <span class="select-menu-item-icon octicon octicon-check"></span>
                  <a href="/blstream/UrbanGame/blob/master/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="master" rel="nofollow" title="master">master</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item ">
                  <span class="select-menu-item-icon octicon octicon-check"></span>
                  <a href="/blstream/UrbanGame/blob/web/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="web" rel="nofollow" title="web">web</a>
                </div> <!-- /.select-menu-item -->
                <div class="select-menu-item js-navigation-item ">
                  <span class="select-menu-item-icon octicon octicon-check"></span>
                  <a href="/blstream/UrbanGame/blob/windows_phone/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" class="js-navigation-open select-menu-item-text js-select-button-text css-truncate-target" data-name="windows_phone" rel="nofollow" title="windows_phone">windows_phone</a>
                </div> <!-- /.select-menu-item -->
            </div>

              <form accept-charset="UTF-8" action="/blstream/UrbanGame/branches" class="js-create-branch select-menu-item select-menu-new-item-form js-navigation-item js-new-item-form" method="post"><div style="margin:0;padding:0;display:inline"><input name="authenticity_token" type="hidden" value="yUFYkIxmHiKZu2/mHwXORKWYuOnoKRGJhgUys+zETxI=" /></div>

                <span class="octicon octicon-git-branch-create select-menu-item-icon"></span>
                <div class="select-menu-item-text">
                  <h4>Create branch: <span class="js-new-item-name"></span></h4>
                  <span class="description">from ‘android’</span>
                </div>
                <input type="hidden" name="name" id="name" class="js-new-item-value">
                <input type="hidden" name="branch" id="branch" value="android" />
                <input type="hidden" name="path" id="branch" value="UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" />
              </form> <!-- /.select-menu-item -->

          </div> <!-- /.select-menu-list -->


          <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket css-truncate" data-tab-filter="tags">
            <div data-filterable-for="commitish-filter-field" data-filterable-type="substring">

            </div>

            <div class="select-menu-no-results">Nothing to show</div>

          </div> <!-- /.select-menu-list -->

        </div> <!-- /.select-menu-modal -->
      </div> <!-- /.select-menu-modal-holder -->
    </div> <!-- /.select-menu -->

  </div> <!-- /.scope -->

  <ul class="tabnav-tabs">
    <li><a href="/blstream/UrbanGame/tree/android" class="selected js-selected-navigation-item tabnav-tab" data-selected-links="repo_source /blstream/UrbanGame/tree/android">Files</a></li>
    <li><a href="/blstream/UrbanGame/commits/android" class="js-selected-navigation-item tabnav-tab" data-selected-links="repo_commits /blstream/UrbanGame/commits/android">Commits</a></li>
    <li><a href="/blstream/UrbanGame/branches" class="js-selected-navigation-item tabnav-tab" data-selected-links="repo_branches /blstream/UrbanGame/branches" rel="nofollow">Branches <span class="counter ">5</span></a></li>
  </ul>

</div>

  
  
  


            
          </div>
        </div><!-- /.repohead -->

        <div id="js-repo-pjax-container" class="container context-loader-container" data-pjax-container>
          


<!-- blob contrib key: blob_contributors:v21:d6bc6118dedd08f9b0ae0177aba68ad0 -->
<!-- blob contrib frag key: views10/v8/blob_contributors:v21:d6bc6118dedd08f9b0ae0177aba68ad0 -->


<div id="slider">
    <div class="frame-meta">

      <p title="This is a placeholder element" class="js-history-link-replace hidden"></p>

        <div class="breadcrumb">
          <span class='bold'><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/blstream/UrbanGame/tree/android" class="js-slide-to" data-branch="android" data-direction="back" itemscope="url"><span itemprop="title">UrbanGame</span></a></span></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/blstream/UrbanGame/tree/android/UrbanGame" class="js-slide-to" data-branch="android" data-direction="back" itemscope="url"><span itemprop="title">UrbanGame</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/blstream/UrbanGame/tree/android/UrbanGame/src" class="js-slide-to" data-branch="android" data-direction="back" itemscope="url"><span itemprop="title">src</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/blstream/UrbanGame/tree/android/UrbanGame/src/com" class="js-slide-to" data-branch="android" data-direction="back" itemscope="url"><span itemprop="title">com</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/blstream/UrbanGame/tree/android/UrbanGame/src/com/blstream" class="js-slide-to" data-branch="android" data-direction="back" itemscope="url"><span itemprop="title">blstream</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/blstream/UrbanGame/tree/android/UrbanGame/src/com/blstream/urbangame" class="js-slide-to" data-branch="android" data-direction="back" itemscope="url"><span itemprop="title">urbangame</span></a></span><span class="separator"> / </span><strong class="final-path">LoginRegisterActivity.java</strong> <span class="js-zeroclipboard zeroclipboard-button" data-clipboard-text="UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" data-copied-hint="copied!" title="copy to clipboard"><span class="octicon octicon-clippy"></span></span>
        </div>

      <a href="/blstream/UrbanGame/find/android" class="js-slide-to" data-hotkey="t" style="display:none">Show File Finder</a>


        <div class="commit commit-loader file-history-tease js-deferred-content" data-url="/blstream/UrbanGame/contributors/android/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java">
          Fetching contributors…

          <div class="participation">
            <p class="loader-loading"><img alt="Octocat-spinner-32-eaf2f5" height="16" src="https://a248.e.akamai.net/assets.github.com/images/spinners/octocat-spinner-32-EAF2F5.gif?1340659530" width="16" /></p>
            <p class="loader-error">Cannot retrieve contributors at this time</p>
          </div>
        </div>

    </div><!-- ./.frame-meta -->

    <div class="frames">
      <div class="frame" data-permalink-url="/blstream/UrbanGame/blob/8909860097313d5da757bb479934f230843cae4e/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" data-title="UrbanGame/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java at android · blstream/UrbanGame · GitHub" data-type="blob">

        <div id="files" class="bubble">
          <div class="file">
            <div class="meta">
              <div class="info">
                <span class="icon"><b class="octicon octicon-file-text"></b></span>
                <span class="mode" title="File Mode">file</span>
                  <span>45 lines (38 sloc)</span>
                <span>1.392 kb</span>
              </div>
              <div class="actions">
                <div class="button-group">
                        <a class="minibutton"
                           href="/blstream/UrbanGame/edit/android/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java"
                           data-method="post" rel="nofollow" data-hotkey="e">Edit</a>
                  <a href="/blstream/UrbanGame/raw/android/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" class="button minibutton " id="raw-url">Raw</a>
                    <a href="/blstream/UrbanGame/blame/android/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" class="button minibutton ">Blame</a>
                  <a href="/blstream/UrbanGame/commits/android/UrbanGame/src/com/blstream/urbangame/LoginRegisterActivity.java" class="button minibutton " rel="nofollow">History</a>
                </div><!-- /.button-group -->
              </div><!-- /.actions -->

            </div>
                <div class="blob-wrapper data type-java js-blob-data">
      <table class="file-code file-diff">
        <tr class="file-code-line">
          <td class="blob-line-nums">
            <span id="L1" rel="#L1">1</span>
<span id="L2" rel="#L2">2</span>
<span id="L3" rel="#L3">3</span>
<span id="L4" rel="#L4">4</span>
<span id="L5" rel="#L5">5</span>
<span id="L6" rel="#L6">6</span>
<span id="L7" rel="#L7">7</span>
<span id="L8" rel="#L8">8</span>
<span id="L9" rel="#L9">9</span>
<span id="L10" rel="#L10">10</span>
<span id="L11" rel="#L11">11</span>
<span id="L12" rel="#L12">12</span>
<span id="L13" rel="#L13">13</span>
<span id="L14" rel="#L14">14</span>
<span id="L15" rel="#L15">15</span>
<span id="L16" rel="#L16">16</span>
<span id="L17" rel="#L17">17</span>
<span id="L18" rel="#L18">18</span>
<span id="L19" rel="#L19">19</span>
<span id="L20" rel="#L20">20</span>
<span id="L21" rel="#L21">21</span>
<span id="L22" rel="#L22">22</span>
<span id="L23" rel="#L23">23</span>
<span id="L24" rel="#L24">24</span>
<span id="L25" rel="#L25">25</span>
<span id="L26" rel="#L26">26</span>
<span id="L27" rel="#L27">27</span>
<span id="L28" rel="#L28">28</span>
<span id="L29" rel="#L29">29</span>
<span id="L30" rel="#L30">30</span>
<span id="L31" rel="#L31">31</span>
<span id="L32" rel="#L32">32</span>
<span id="L33" rel="#L33">33</span>
<span id="L34" rel="#L34">34</span>
<span id="L35" rel="#L35">35</span>
<span id="L36" rel="#L36">36</span>
<span id="L37" rel="#L37">37</span>
<span id="L38" rel="#L38">38</span>
<span id="L39" rel="#L39">39</span>
<span id="L40" rel="#L40">40</span>
<span id="L41" rel="#L41">41</span>
<span id="L42" rel="#L42">42</span>
<span id="L43" rel="#L43">43</span>
<span id="L44" rel="#L44">44</span>
<span id="L45" rel="#L45">45</span>

          </td>
          <td class="blob-line-code">
                  <div class="highlight"><pre><div class='line' id='LC1'><span class="kn">package</span> <span class="n">com</span><span class="o">.</span><span class="na">blstream</span><span class="o">.</span><span class="na">urbangame</span><span class="o">;</span></div><div class='line' id='LC2'><br/></div><div class='line' id='LC3'><span class="kn">import</span> <span class="nn">android.os.Bundle</span><span class="o">;</span></div><div class='line' id='LC4'><span class="kn">import</span> <span class="nn">android.support.v4.view.ViewPager</span><span class="o">;</span></div><div class='line' id='LC5'><br/></div><div class='line' id='LC6'><span class="kn">import</span> <span class="nn">com.actionbarsherlock.app.SherlockFragmentActivity</span><span class="o">;</span></div><div class='line' id='LC7'><span class="kn">import</span> <span class="nn">com.actionbarsherlock.view.Menu</span><span class="o">;</span></div><div class='line' id='LC8'><span class="kn">import</span> <span class="nn">com.actionbarsherlock.view.MenuInflater</span><span class="o">;</span></div><div class='line' id='LC9'><span class="kn">import</span> <span class="nn">com.actionbarsherlock.view.MenuItem</span><span class="o">;</span></div><div class='line' id='LC10'><span class="kn">import</span> <span class="nn">com.blstream.urbangame.fragments.LoginRegisterPageAdapter</span><span class="o">;</span></div><div class='line' id='LC11'><br/></div><div class='line' id='LC12'><span class="kd">public</span> <span class="kd">class</span> <span class="nc">LoginRegisterActivity</span> <span class="kd">extends</span> <span class="n">SherlockFragmentActivity</span> <span class="o">{</span></div><div class='line' id='LC13'>	<span class="kd">private</span> <span class="n">LoginRegisterPageAdapter</span> <span class="n">sectionsPagerAdapter</span><span class="o">;</span></div><div class='line' id='LC14'>	<span class="kd">private</span> <span class="n">ViewPager</span> <span class="n">viewPager</span><span class="o">;</span></div><div class='line' id='LC15'><br/></div><div class='line' id='LC16'>	<span class="nd">@Override</span></div><div class='line' id='LC17'>	<span class="kd">protected</span> <span class="kt">void</span> <span class="nf">onCreate</span><span class="o">(</span><span class="n">Bundle</span> <span class="n">savedInstanceState</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC18'>		<span class="kd">super</span><span class="o">.</span><span class="na">onCreate</span><span class="o">(</span><span class="n">savedInstanceState</span><span class="o">);</span></div><div class='line' id='LC19'>		<span class="n">setSupportProgressBarVisibility</span><span class="o">(</span><span class="kc">true</span><span class="o">);</span></div><div class='line' id='LC20'>		<span class="n">getSupportActionBar</span><span class="o">().</span><span class="na">setDisplayHomeAsUpEnabled</span><span class="o">(</span><span class="kc">true</span><span class="o">);</span></div><div class='line' id='LC21'>		<span class="n">setContentView</span><span class="o">(</span><span class="n">R</span><span class="o">.</span><span class="na">layout</span><span class="o">.</span><span class="na">activity_login_register</span><span class="o">);</span></div><div class='line' id='LC22'><br/></div><div class='line' id='LC23'>		<span class="n">sectionsPagerAdapter</span> <span class="o">=</span> <span class="k">new</span> <span class="n">LoginRegisterPageAdapter</span><span class="o">(</span><span class="n">getSupportFragmentManager</span><span class="o">(),</span> <span class="n">LoginRegisterActivity</span><span class="o">.</span><span class="na">this</span><span class="o">);</span></div><div class='line' id='LC24'>		<span class="n">viewPager</span> <span class="o">=</span> <span class="o">(</span><span class="n">ViewPager</span><span class="o">)</span> <span class="n">findViewById</span><span class="o">(</span><span class="n">R</span><span class="o">.</span><span class="na">id</span><span class="o">.</span><span class="na">view_pager</span><span class="o">);</span></div><div class='line' id='LC25'>		<span class="n">viewPager</span><span class="o">.</span><span class="na">setAdapter</span><span class="o">(</span><span class="n">sectionsPagerAdapter</span><span class="o">);</span></div><div class='line' id='LC26'>	<span class="o">}</span></div><div class='line' id='LC27'><br/></div><div class='line' id='LC28'>	<span class="nd">@Override</span></div><div class='line' id='LC29'>	<span class="kd">public</span> <span class="kt">boolean</span> <span class="nf">onCreateOptionsMenu</span><span class="o">(</span><span class="n">Menu</span> <span class="n">menu</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC30'>		<span class="n">MenuInflater</span> <span class="n">menuInflater</span> <span class="o">=</span> <span class="n">getSupportMenuInflater</span><span class="o">();</span></div><div class='line' id='LC31'>		<span class="n">menuInflater</span><span class="o">.</span><span class="na">inflate</span><span class="o">(</span><span class="n">R</span><span class="o">.</span><span class="na">menu</span><span class="o">.</span><span class="na">top_bar_menu_more</span><span class="o">,</span> <span class="n">menu</span><span class="o">);</span></div><div class='line' id='LC32'>		<span class="k">return</span> <span class="kc">true</span><span class="o">;</span></div><div class='line' id='LC33'>	<span class="o">}</span></div><div class='line' id='LC34'><br/></div><div class='line' id='LC35'>	<span class="nd">@Override</span></div><div class='line' id='LC36'>	<span class="kd">public</span> <span class="kt">boolean</span> <span class="nf">onMenuItemSelected</span><span class="o">(</span><span class="kt">int</span> <span class="n">featureId</span><span class="o">,</span> <span class="n">MenuItem</span> <span class="n">item</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC37'>		<span class="kt">int</span> <span class="n">itemId</span> <span class="o">=</span> <span class="n">item</span><span class="o">.</span><span class="na">getItemId</span><span class="o">();</span></div><div class='line' id='LC38'>		<span class="k">switch</span> <span class="o">(</span><span class="n">itemId</span><span class="o">)</span> <span class="o">{</span></div><div class='line' id='LC39'>			<span class="k">case</span> <span class="n">android</span><span class="o">.</span><span class="na">R</span><span class="o">.</span><span class="na">id</span><span class="o">.</span><span class="na">home</span><span class="o">:</span></div><div class='line' id='LC40'>				<span class="n">finish</span><span class="o">();</span></div><div class='line' id='LC41'>				<span class="k">break</span><span class="o">;</span></div><div class='line' id='LC42'>		<span class="o">}</span></div><div class='line' id='LC43'>		<span class="k">return</span> <span class="kc">true</span><span class="o">;</span></div><div class='line' id='LC44'>	<span class="o">}</span></div><div class='line' id='LC45'><span class="o">}</span></div></pre></div>
          </td>
        </tr>
      </table>
  </div>

          </div>
        </div>

        <a href="#jump-to-line" rel="facebox" data-hotkey="l" class="js-jump-to-line" style="display:none">Jump to Line</a>
        <div id="jump-to-line" style="display:none">
          <h2>Jump to Line</h2>
          <form accept-charset="UTF-8" class="js-jump-to-line-form">
            <input class="textfield js-jump-to-line-field" type="text">
            <div class="full-button">
              <button type="submit" class="button">Go</button>
            </div>
          </form>
        </div>

      </div>
    </div>
</div>

<div id="js-frame-loading-template" class="frame frame-loading large-loading-area" style="display:none;">
  <img class="js-frame-loading-spinner" src="https://a248.e.akamai.net/assets.github.com/images/spinners/octocat-spinner-128.gif?1347543525" height="64" width="64">
</div>


        </div>
      </div>
      <div class="modal-backdrop"></div>
    </div>

      <div id="footer-push"></div><!-- hack for sticky footer -->
    </div><!-- end of wrapper - hack for sticky footer -->

      <!-- footer -->
      <div id="footer">
  <div class="container clearfix">

      <dl class="footer_nav">
        <dt>GitHub</dt>
        <dd><a href="https://github.com/about">About us</a></dd>
        <dd><a href="https://github.com/blog">Blog</a></dd>
        <dd><a href="https://github.com/contact">Contact &amp; support</a></dd>
        <dd><a href="http://enterprise.github.com/">GitHub Enterprise</a></dd>
        <dd><a href="http://status.github.com/">Site status</a></dd>
      </dl>

      <dl class="footer_nav">
        <dt>Applications</dt>
        <dd><a href="http://mac.github.com/">GitHub for Mac</a></dd>
        <dd><a href="http://windows.github.com/">GitHub for Windows</a></dd>
        <dd><a href="http://eclipse.github.com/">GitHub for Eclipse</a></dd>
        <dd><a href="http://mobile.github.com/">GitHub mobile apps</a></dd>
      </dl>

      <dl class="footer_nav">
        <dt>Services</dt>
        <dd><a href="http://get.gaug.es/">Gauges: Web analytics</a></dd>
        <dd><a href="http://speakerdeck.com">Speaker Deck: Presentations</a></dd>
        <dd><a href="https://gist.github.com">Gist: Code snippets</a></dd>
        <dd><a href="http://jobs.github.com/">Job board</a></dd>
      </dl>

      <dl class="footer_nav">
        <dt>Documentation</dt>
        <dd><a href="http://help.github.com/">GitHub Help</a></dd>
        <dd><a href="http://developer.github.com/">Developer API</a></dd>
        <dd><a href="http://github.github.com/github-flavored-markdown/">GitHub Flavored Markdown</a></dd>
        <dd><a href="http://pages.github.com/">GitHub Pages</a></dd>
      </dl>

      <dl class="footer_nav">
        <dt>More</dt>
        <dd><a href="http://training.github.com/">Training</a></dd>
        <dd><a href="https://github.com/edu">Students &amp; teachers</a></dd>
        <dd><a href="http://shop.github.com">The Shop</a></dd>
        <dd><a href="/plans">Plans &amp; pricing</a></dd>
        <dd><a href="http://octodex.github.com/">The Octodex</a></dd>
      </dl>

      <hr class="footer-divider">


    <p class="right">&copy; 2013 <span title="0.26672s from fe17.rs.github.com">GitHub</span>, Inc. All rights reserved.</p>
    <a class="left" href="https://github.com/">
      <span class="mega-octicon octicon-mark-github"></span>
    </a>
    <ul id="legal">
        <li><a href="https://github.com/site/terms">Terms of Service</a></li>
        <li><a href="https://github.com/site/privacy">Privacy</a></li>
        <li><a href="https://github.com/security">Security</a></li>
    </ul>

  </div><!-- /.container -->

</div><!-- /.#footer -->


    <div class="fullscreen-overlay js-fullscreen-overlay" id="fullscreen_overlay">
  <div class="fullscreen-container js-fullscreen-container">
    <div class="textarea-wrap">
      <textarea name="fullscreen-contents" id="fullscreen-contents" class="js-fullscreen-contents" placeholder="" data-suggester="fullscreen_suggester"></textarea>
          <div class="suggester-container">
              <div class="suggester fullscreen-suggester js-navigation-container" id="fullscreen_suggester"
                 data-url="/blstream/UrbanGame/suggestions/commit">
              </div>
          </div>
    </div>
  </div>
  <div class="fullscreen-sidebar">
    <a href="#" class="exit-fullscreen js-exit-fullscreen tooltipped leftwards" title="Exit Zen Mode">
      <span class="mega-octicon octicon-screen-normal"></span>
    </a>
    <a href="#" class="theme-switcher js-theme-switcher tooltipped leftwards"
      title="Switch themes">
      <span class="octicon octicon-color-mode"></span>
    </a>
  </div>
</div>



    <div id="ajax-error-message" class="flash flash-error">
      <span class="octicon octicon-alert"></span>
      Something went wrong with that request. Please try again.
      <a href="#" class="octicon octicon-remove-close ajax-error-dismiss"></a>
    </div>

    
    
    <span id='server_response_time' data-time='0.26724' data-host='fe17'></span>
    
  </body>
</html>


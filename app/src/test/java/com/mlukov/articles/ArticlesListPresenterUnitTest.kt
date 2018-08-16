package com.mlukov.articles

import com.mlukov.articles.api.model.ArticleApiData
import com.mlukov.articles.domain.interactors.ArticleInteractor
import com.mlukov.articles.domain.models.ArticleData
import com.mlukov.articles.domain.models.ArticleDataList
import com.mlukov.articles.domain.repositories.articles.IArticlesApiRepository
import com.mlukov.articles.domain.repositories.local.ILocalStorageRepository
import com.mlukov.articles.presentation.articles.list.model.ArticleViewData
import com.mlukov.articles.presentation.articles.list.presenter.ArticlesListPresenter
import com.mlukov.articles.presentation.articles.list.view.IArticlesListView
import com.mlukov.articles.presentation.articles.list.model.ArticleListViewModel
import com.mlukov.articles.presentation.providers.INetworkInfoProvider
import com.mlukov.articles.presentation.providers.IResourceProvider
import com.mlukov.articles.utils.ISchedulersProvider

import org.junit.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import java.util.ArrayList
import java.util.Date

import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler

import org.junit.Assert.assertEquals

class ArticlesListPresenterUnitTest {

    @Mock
    private val articlesView: IArticlesListView? = null

    @Mock
    val resourceProvider : IResourceProvider? = null

    @Mock
    val networkInfoProvider : INetworkInfoProvider ? = null

    @Mock
    private val localStorageRepository: ILocalStorageRepository? = null

    @Mock
    private val articlesApiRepository: IArticlesApiRepository? = null

    @Mock
    private val articlesInteractorMock: ArticleInteractor? = null

    @Mock
    private val schedulersProvider: ISchedulersProvider? = null

    @InjectMocks
    private val articlesPresenter: ArticlesListPresenter? = null

    @InjectMocks
    private val articlesInteractor: ArticleInteractor? = null

    private val testScheduler = TestScheduler()

    private val cachedItems = ArticleDataList()
    private val serverItems = ArrayList<ArticleApiData>()

    private val articleItems = ArrayList<ArticleViewData>()

    @Before
    fun setup() {

        MockitoAnnotations.initMocks(this)

        Mockito.doReturn(testScheduler).`when`<ISchedulersProvider>(schedulersProvider).ioScheduler()
        Mockito.doReturn(testScheduler).`when`<ISchedulersProvider>(schedulersProvider).uiScheduler()
        Mockito.doReturn(testScheduler).`when`<ISchedulersProvider>(schedulersProvider).computationScheduler()

        val articleApiData = ArticleApiData()

        articleApiData.id = 2
        articleApiData.title = "Title2"
        articleApiData.subtitle = "Subtitle2"
        articleApiData.date = Date("1982/01/21 18:41")

        val articleData = ArticleData()
        articleData.id = 1
        articleData.title = "TITLE1"
        articleData.subtitle = "SUBTTITLE1"
        articleData.date = Date("1980/01/21 18:41")

        val cachedData = ArrayList<ArticleData>()
        cachedData.add(articleData)
        cachedItems.articles = cachedData
        serverItems.add(articleApiData)
    }

    @Test
    fun Should_Load_Articles_List_Into_View() {

        Mockito.doReturn(Single.just(cachedItems)).`when`<ILocalStorageRepository>(localStorageRepository).getArticleDataListFromCache()
        Mockito.doReturn(Single.just<List<ArticleApiData>>(serverItems)).`when`<IArticlesApiRepository>(articlesApiRepository).getArticleList()
        Mockito.doAnswer(object : Answer<Single<ArticleDataList>> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList> {
                return articlesInteractor!!.getArticleList()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).getArticleList()

        Mockito.doAnswer(object : Answer<ArrayList<ArticleViewData>> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): ArrayList<ArticleViewData>? {

                val arrayList = invocation.getArgument<ArrayList<ArticleViewData>>(0)
                articleItems.clear()
                articleItems.addAll(arrayList)
                return null
            }
        }).`when`<IArticlesListView>(articlesView).onArticlesLoaded(Mockito.any())

        articlesPresenter!!.loadArticles(false)
        testScheduler.triggerActions()

        val repositoryItem = articleItems[0]

        Assert.assertNotNull(repositoryItem)
    }

    @Test
    fun Should_Load_Articles_List_From_Cache() {

        Mockito.doReturn(Single.just(cachedItems)).`when`<ILocalStorageRepository>(localStorageRepository).getArticleDataListFromCache()
        Mockito.doReturn(Single.just<List<ArticleApiData>>(serverItems)).`when`<IArticlesApiRepository>(articlesApiRepository).getArticleList()
        Mockito.doAnswer(object : Answer  <Single<ArticleDataList>> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock):  Single<ArticleDataList> {
                return articlesInteractor!!.getArticleList()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).getArticleList()

        Mockito.doAnswer(object : Answer < ArticleListViewModel> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): ArticleListViewModel? {

                val arrayList = invocation.getArgument<ArrayList<ArticleViewData>>(0)
                articleItems.clear()
                articleItems.addAll(arrayList)
                return null
            }
        }).`when`<IArticlesListView>(articlesView).onArticlesLoaded(Mockito.any())

        articlesPresenter!!.loadArticles(false)
        testScheduler.triggerActions()

        val repositoryItem = articleItems[0]

        Assert.assertEquals("Item not loaded from cache", "TITLE1", repositoryItem.title)
    }

    @Test
    fun Should_Load_Articles_List_From_Server_Refresh_True() {

        Mockito.doReturn(Single.just(cachedItems)).`when`<ILocalStorageRepository>(localStorageRepository).getArticleDataListFromCache()
        Mockito.doReturn(Single.just<List<ArticleApiData>>(serverItems)).`when`<IArticlesApiRepository>(articlesApiRepository).getArticleList()

        Mockito.doAnswer(object : Answer <Any>{
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any? {

                cachedItems.articles!!.clear()
                return null
            }
        }).`when`<ILocalStorageRepository>(localStorageRepository).dropArticleDataListCache()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList> > {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList>  {
                return articlesInteractor!!.getArticleList()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).getArticleList()

        Mockito.doAnswer(object : Answer <Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any {
                return articlesInteractor!!.clearCache()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).clearCache()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList> > {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList>   {

                val articles = invocation.getArgument<ArticleDataList>(0)
                return Single.just(articles)
            }
        }).`when`<ILocalStorageRepository>(localStorageRepository).addArticleDataListToCache(Mockito.any(ArticleDataList::class.java))

        Mockito.doAnswer(object : Answer <ArrayList<ArticleViewData>> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): ArrayList<ArticleViewData>? {

                val arrayList = invocation.getArgument<ArrayList<ArticleViewData>>(0)
                articleItems.clear()
                articleItems.addAll(arrayList)
                return null
            }
        }).`when`<IArticlesListView>(articlesView).onArticlesLoaded(Mockito.any())

        articlesPresenter!!.loadArticles(true)
        testScheduler.triggerActions()

        val repositoryItem = articleItems[0]

        Assert.assertEquals("Item not loaded from cache", "TITLE1", repositoryItem.title)
    }

    @Test
    fun Should_Load_Articles_List_From_Server_Refresh_False_No_Cached_Items() {

        Mockito.doReturn(Single.just(ArticleDataList.empty())).`when`<ILocalStorageRepository>(localStorageRepository).getArticleDataListFromCache()
        Mockito.doReturn(Single.just<List<ArticleApiData>>(serverItems)).`when`<IArticlesApiRepository>(articlesApiRepository).getArticleList()

        Mockito.doAnswer(object : Answer <Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any? {

                cachedItems.articles!!.clear()
                return null
            }
        }).`when`<ILocalStorageRepository>(localStorageRepository).dropArticleDataListCache()

        Mockito.doAnswer(object : Answer <Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any {
                return articlesInteractor!!.getArticleList()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).getArticleList()

        Mockito.doAnswer(object : Answer <Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any {
                return articlesInteractor!!.clearCache()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).clearCache()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList> >{
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList>  {

                val articles = invocation.getArgument<ArticleDataList>(0)
                return Single.just(articles)
            }
        }).`when`<ILocalStorageRepository>(localStorageRepository).addArticleDataListToCache(Mockito.any(ArticleDataList::class.java))

        Mockito.doAnswer(object : Answer <ArrayList<ArticleViewData>> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): ArrayList<ArticleViewData>? {

                val arrayList = invocation.getArgument<ArrayList<ArticleViewData>>(0)
                articleItems.clear()
                articleItems.addAll(arrayList)
                return null
            }
        }).`when`<IArticlesListView>(articlesView).onArticlesLoaded(Mockito.any())

        articlesPresenter!!.loadArticles(false)
        testScheduler.triggerActions()

        val repositoryItem = articleItems[0]

        Assert.assertEquals("Item not loaded from cache", "Title2", repositoryItem.title)
    }

    @Test
    fun Verify_Method_Calls_When_Loading_From_Cache() {

        Mockito.doReturn(Single.just(cachedItems)).`when`<ILocalStorageRepository>(localStorageRepository).getArticleDataListFromCache()
        Mockito.doReturn(Single.just<List<ArticleApiData>>(serverItems)).`when`<IArticlesApiRepository>(articlesApiRepository).getArticleList()
        Mockito.doAnswer(object : Answer <Single<ArticleDataList> > {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList> {
                return articlesInteractor!!.getArticleList()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).getArticleList()

        Mockito.doAnswer(object : Answer <ArrayList<ArticleViewData>> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): ArrayList<ArticleViewData>? {

                val arrayList = invocation.getArgument<ArrayList<ArticleViewData>>(0)
                articleItems.clear()
                articleItems.addAll(arrayList)
                return null
            }
        }).`when`<IArticlesListView>(articlesView).onArticlesLoaded(Mockito.any())

        articlesPresenter!!.loadArticles(false)
        testScheduler.triggerActions()

        Mockito.verify<ArticleInteractor>(articlesInteractorMock, Mockito.times(1)).getArticleList()
        Mockito.verify<ArticleInteractor>(articlesInteractorMock, Mockito.times(0)).clearCache()
        Mockito.verify<ILocalStorageRepository>(localStorageRepository, Mockito.times(1)).getArticleDataListFromCache()
        Mockito.verify<IArticlesListView>(articlesView, Mockito.times(2)).onLoadingStateChange(Mockito.anyBoolean())
        Mockito.verify<IArticlesListView>(articlesView, Mockito.times(1)).onArticlesLoaded(Mockito.any())
    }

    @Test
    fun Verify_Method_Calls_When_Loading_From_Server_Refresh_True() {

        Mockito.doReturn(Single.just(cachedItems)).`when`<ILocalStorageRepository>(localStorageRepository).getArticleDataListFromCache()
        Mockito.doReturn(Single.just<List<ArticleApiData>>(serverItems)).`when`<IArticlesApiRepository>(articlesApiRepository).getArticleList()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList> > {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList> ? {

                cachedItems.articles!!.clear()
                return null
            }
        }).`when`<ILocalStorageRepository>(localStorageRepository).dropArticleDataListCache()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList> > {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList>  {
                return articlesInteractor!!.getArticleList()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).getArticleList()

        Mockito.doAnswer(object : Answer <Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any {
                return articlesInteractor!!.clearCache()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).clearCache()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList> > {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList>  {

                val articleDataList = invocation.getArgument<ArticleDataList>(0)
                return Single.just(articleDataList)
            }
        }).`when`<ILocalStorageRepository>(localStorageRepository).addArticleDataListToCache(Mockito.any(ArticleDataList::class.java))

        Mockito.doAnswer(object : Answer <ArrayList<ArticleViewData>> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): ArrayList<ArticleViewData>? {

                val arrayList = invocation.getArgument<ArrayList<ArticleViewData>>(0)
                articleItems.clear()
                articleItems.addAll(arrayList)
                return null
            }
        }).`when`<IArticlesListView>(articlesView).onArticlesLoaded(Mockito.any())

        articlesPresenter!!.loadArticles(true)
        testScheduler.triggerActions()

        Mockito.verify<ArticleInteractor>(articlesInteractorMock, Mockito.times(1)).getArticleList()
        Mockito.verify<ArticleInteractor>(articlesInteractorMock, Mockito.times(1)).clearCache()
        Mockito.verify<ILocalStorageRepository>(localStorageRepository, Mockito.times(1)).addArticleDataListToCache(Mockito.any(ArticleDataList::class.java))
        Mockito.verify<IArticlesApiRepository>(articlesApiRepository, Mockito.times(1)).getArticleList()
        Mockito.verify<IArticlesListView>(articlesView, Mockito.times(2)).onLoadingStateChange(Mockito.anyBoolean())
        Mockito.verify<IArticlesListView>(articlesView, Mockito.times(1)).onArticlesLoaded(Mockito.any())
    }

    @Test
    fun Verify_Method_Calls_When_Loading_From_Server_Refresh_False() {

        Mockito.doReturn(Single.just(ArticleDataList.empty())).`when`<ILocalStorageRepository>(localStorageRepository).getArticleDataListFromCache()
        Mockito.doReturn(Single.just<List<ArticleApiData>>(serverItems)).`when`<IArticlesApiRepository>(articlesApiRepository).getArticleList()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList>>{
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList> ? {

                cachedItems.articles!!.clear()
                return null
            }
        }).`when`<ILocalStorageRepository>(localStorageRepository).dropArticleDataListCache()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList> > {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList>  {
                return articlesInteractor!!.getArticleList()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).getArticleList()

        Mockito.doAnswer(object : Answer<Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any {
                return articlesInteractor!!.clearCache()
            }
        }).`when`<ArticleInteractor>(articlesInteractorMock).clearCache()

        Mockito.doAnswer(object : Answer <Single<ArticleDataList>>{
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Single<ArticleDataList> {

                val articleDataList = invocation.getArgument<ArticleDataList>(0)
                return Single.just(articleDataList)
            }
        }).`when`<ILocalStorageRepository>(localStorageRepository).addArticleDataListToCache(Mockito.any(ArticleDataList::class.java))

        Mockito.doAnswer(object : Answer <ArrayList<ArticleViewData>>{
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): ArrayList<ArticleViewData>? {

                val arrayList = invocation.getArgument<ArrayList<ArticleViewData>>(0)
                articleItems.clear()
                articleItems.addAll(arrayList)
                return null
            }
        }).`when`<IArticlesListView>(articlesView).onArticlesLoaded(Mockito.any())

        articlesPresenter!!.loadArticles(false)
        testScheduler.triggerActions()

        Mockito.verify<ArticleInteractor>(articlesInteractorMock, Mockito.times(1)).getArticleList()
        Mockito.verify<ArticleInteractor>(articlesInteractorMock, Mockito.times(0)).clearCache()
        Mockito.verify<ILocalStorageRepository>(localStorageRepository, Mockito.times(1)).getArticleDataListFromCache()
        Mockito.verify<ILocalStorageRepository>(localStorageRepository, Mockito.times(1)).addArticleDataListToCache(Mockito.any(ArticleDataList::class.java))
        Mockito.verify<IArticlesApiRepository>(articlesApiRepository, Mockito.times(1)).getArticleList()
        Mockito.verify<IArticlesListView>(articlesView, Mockito.times(2)).onLoadingStateChange(Mockito.anyBoolean())
        Mockito.verify<IArticlesListView>(articlesView, Mockito.times(1)).onArticlesLoaded(Mockito.any())
    }
}

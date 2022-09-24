package com.example.bankuishchallenge

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bankuishchallenge.ui.RepoListFragment
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/* NavigationTest of Navigation Component using TestNavHostController
 * and FragmentScenario
 */
@RunWith(AndroidJUnit4::class)
class NavigationTestsNavComp {
    private lateinit var navController: TestNavHostController

    private lateinit var repoListScenario: FragmentScenario<RepoListFragment>

    @Before
    fun setup() {
        //Create a test instance of the navigation controller.
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        //Here we specify that we want to launch the RepoListFragment.
        //We have to pass the app's theme so that the UI components know which theme to use or the test may crash.
        repoListScenario = launchFragmentInContainer(themeResId =
        R.style.Theme_BankuishChallenge)

        //Declare which navigation graph we want the nav controller to use for the fragment launched
        repoListScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }

    //Test navigate to repoDetail using test Nav host controller
    @Test
    fun navigate_to_repoDetails_nav_component() {

        //Wait for recyclerView to load data
        Thread.sleep(3000)
        //Trigger the event that prompts the navigation
        // In this case the event that prompts the navigation is clicking in the first element
        // of the recyclerView
        Espresso.onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
        //Assert that current destination is movieDetailFragment
        Assert.assertEquals(navController.currentDestination?.id, R.id.repoDetailsFragment)
    }
}
package com.gmail.chickenpowerrr.ranksync.api.player;

/**
 * This interface contains all of the basic information a Player needs to link his account between
 * multiple services
 *
 * @author Chickenpowerrr
 * @since 1.0.0
 */
public interface LinkInfo {

  /**
   * Get the name of the service that should get linked
   *
   * @return the name of the service that should get linked
   */
  String getName();

  /**
   * Get the explanation about how to link an account
   *
   * @return the explanation about how to link an account
   */
  String getLinkExplanation();
}
